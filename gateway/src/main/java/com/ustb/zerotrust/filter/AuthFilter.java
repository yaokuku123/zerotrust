package com.ustb.zerotrust.filter;

import com.ustb.zerotrust.config.RsaKeyProperties;
import com.ustb.zerotrust.domain.Payload;
import com.ustb.zerotrust.domain.ResponseCodeEnum;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.domain.SysUser;
import com.ustb.zerotrust.utils.JsonUtils;
import com.ustb.zerotrust.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: AuthFilter
 * Author: yaoqijun
 * Date: 2021/5/25 16:43
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RsaKeyProperties prop;


    /**
     * gateway过滤器
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取 request，response和uri
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().getPath();
        //放行路径的白名单
        if (uri.indexOf("/auth-org-server/login") >= 0) {
            return chain.filter(exchange);
        }
        //尝试获取token
        String token = (String) redisTemplate.opsForHash().get("userToken", "token");
        //没有获得token
        if (StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return getVoidMono(response, ResponseCodeEnum.TOKEN_MISSION);
        }
        //验证token
        String username = null;
        String cardId = null;
        Integer gender = null;
        try {
            //从token中获取关键数据，传递给后面的资源模块
            Payload<SysUser> info = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), SysUser.class);
            username = info.getUserInfo().getUsername();
            cardId = info.getUserInfo().getCardId();
            gender = info.getUserInfo().getGender();
        } catch (Exception e) {
            return getVoidMono(response, ResponseCodeEnum.TOKEN_INVALID);
        }
        //添加关键信息到header中，根据资源模块的需求而定
        ServerHttpRequest mutableReq = request.mutate().header("username", username)
                .header("cardId", cardId).header("gender", gender.toString()).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
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

    @Override
    public int getOrder() {
        return -100;
    }
}
