package com.ustb.zerotrust.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.zerotrust.domain.*;
import com.ustb.zerotrust.service.GrantFeignClient;
import com.ustb.zerotrust.utils.JsonUtils;
import com.ustb.zerotrust.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: VerifyFilter
 * Author: yaoqijun
 * Date: 2021/6/17 16:09
 */
@Component
@AllArgsConstructor
public class GrantVerifyFilter implements GlobalFilter, Ordered {

    @Autowired
    private GrantFeignClient grantFeignClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String method = request.getMethodValue();
        String contentType = request.getHeaders().getFirst("Content-Type");
        if ("POST".equals(method)) {
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        try {
                            //获取请求体的json数据
                            String bodyString = new String(bytes, "utf-8");
                            //将json数据转换为自定义请求对象格式
                            RequestGrant requestGrant = new ObjectMapper().readValue(bodyString, RequestGrant.class);
                            //远程调用监控平台
                            ResponseResult grantResult = grantFeignClient.Verify(requestGrant);
                            System.out.println(grantResult.getData());

                            exchange.getAttributes().put("POST_BODY", bodyString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        DataBufferUtils.release(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory()
                                    .wrap(bytes);
                            return Mono.just(buffer);
                        });

                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };
                        return chain.filter(exchange.mutate().request(mutatedRequest)
                                .build());
                    });
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -200;
    }

    /**
     * 错误处理方法
     *
     * @param serverHttpResponse
     * @param responseCodeEnum
     * @return
     */
    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, ResponseCodeEnum responseCodeEnum) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        ResponseResult responseResult = ResponseResult.error(responseCodeEnum.getCode(), responseCodeEnum.getMessage());
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JsonUtils.toString(responseResult).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

}