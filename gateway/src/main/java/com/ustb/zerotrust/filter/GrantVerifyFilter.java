package com.ustb.zerotrust.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.zerotrust.domain.*;
import com.ustb.zerotrust.service.DataCleanFeignClient;
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

    @Autowired
    private DataCleanFeignClient dataCleanFeignClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String method = request.getMethodValue();
        String contentType = request.getHeaders().getFirst("Content-Type");

        //放行路径的白名单
        String uri = request.getURI().getPath();
        if (uri.indexOf("/soft-test-center/") >= 0) {
            return chain.filter(exchange);
        }


        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    try {
                        //获取请求体的json数据
                        String bodyString = new String(bytes, "utf-8");
                        //将json数据转换为自定义请求对象格式
                        RequestGrant requestGrant = new ObjectMapper().readValue(bodyString, RequestGrant.class);
                        //清洗数据
                        ResponseResult dataCleanResult = dataCleanFeignClient.findExtractData(requestGrant.getSoftName());
                        System.out.println(dataCleanResult.getSuccess());
                        if (dataCleanResult.getSuccess()){
                            exchange.getAttributes().put("POST_BODY", bodyString);
                        }else {
                            return getVoidMono(response);
                        }
                        //远程调用监控平台
                        ResponseResult grantResult = grantFeignClient.Verify(requestGrant);
                        System.out.println(grantResult.getData().get("flag"));
                        if ((Boolean) grantResult.getData().get("flag")) {
                            exchange.getAttributes().put("POST_BODY", bodyString);
                        }else {
                            //failjson return
                            return getVoidMono(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //failjson return
                        return getVoidMono(response);
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

    @Override
    public int getOrder() {
        return -200;
    }

    /**
     * 错误处理方法
     *
     * @param serverHttpResponse
     * @return
     */
    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        ResponseResult responseResult = ResponseResult.error();
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JsonUtils.toString(responseResult).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

}