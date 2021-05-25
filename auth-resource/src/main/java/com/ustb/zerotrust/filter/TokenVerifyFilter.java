package com.ustb.zerotrust.filter;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ustb.zerotrust.config.RsaKeyProperties;
//import com.ustb.zerotrust.domain.Payload;
//import com.ustb.zerotrust.domain.ResponseCodeEnum;
//import com.ustb.zerotrust.domain.ResponseResult;
//import com.ustb.zerotrust.domain.SysUser;
//import com.ustb.zerotrust.utils.JwtUtils;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: TokenVerifyFilter
 * Author: yaoqijun
 * Date: 2021/2/25 10:15
 */
//public class TokenVerifyFilter extends BasicAuthenticationFilter {
//    private RsaKeyProperties prop;
//    private RedisTemplate redisTemplate;
//
//    public TokenVerifyFilter(AuthenticationManager authenticationManager, RsaKeyProperties prop, RedisTemplate redisTemplate) {
//        super(authenticationManager);
//        this.prop = prop;
//        this.redisTemplate = redisTemplate;
//    }
//
//    //过滤请求
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//            //请求体的头中是否包含Authorization
////            String header = request.getHeader("Authorization");
//            //Authorization中是否包含Bearer，不包含直接返回
////            if (header == null || !header.startsWith("Bearer ")) {
////                ResponseResult result = ResponseResult.error(ResponseCodeEnum.TOKEN_MISSION.getCode(), ResponseCodeEnum.TOKEN_MISSION.getMessage());
////                responseJson(response,result);
////                chain.doFilter(request, response);
////                return;
////            }
//
//            //从redis中获取token信息
//            String token = (String) redisTemplate.opsForHash().get("tokenHash", "token");
//            if (token == null){
//                ResponseResult result = ResponseResult.error(ResponseCodeEnum.TOKEN_MISSION.getCode(), ResponseCodeEnum.TOKEN_MISSION.getMessage());
//                responseJson(response,result);
//                chain.doFilter(request, response);
//                return;
//            }
//            //获取权限失败，会抛出异常
//            UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
//            ////获取后，将Authentication写入SecurityContextHolder中供后序使用
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            chain.doFilter(request, response);
//        } catch (Exception e) {
//            ResponseResult result = ResponseResult.error(ResponseCodeEnum.TOKEN_INVALID.getCode(), ResponseCodeEnum.TOKEN_INVALID.getMessage());
//            responseJson(response,result);
//            e.printStackTrace();
//        }
//    }
//
//    //未登录提示
//    private void responseJson(HttpServletResponse response,ResponseResult result) {
//        try {
//            //未登录提示
//            response.setContentType("application/json;charset=utf-8");
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            PrintWriter out = response.getWriter();
//            out.write(new ObjectMapper().writeValueAsString(result));
//            out.flush();
//            out.close();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }
//
//    //获取token，并得到用户信息
//    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
//        if (token != null) {
//            //通过token解析出载荷信息
//            Payload<SysUser> payload = JwtUtils.getInfoFromToken(token, prop.getPublicKey(), SysUser.class);
//            SysUser user = payload.getUserInfo();
//            //不为null，返回
//            if (user != null) {
//                return new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
//            }
//            return null;
//        }
//        return null;
//    }
//
//}
