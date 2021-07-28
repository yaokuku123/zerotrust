package com.ustb.zerotrust.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.zerotrust.config.RsaKeyProperties;
import com.ustb.zerotrust.domain.ResponseCodeEnum;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.domain.SysUser;
import com.ustb.zerotrust.mapper.UserMapper;
import com.ustb.zerotrust.utils.JwtUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: TokenLoginFilter
 * Author: yaoqijun
 * Date: 2021/2/25 9:55
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private RsaKeyProperties prop;
    private RedisTemplate redisTemplate;
    private UserMapper userMapper;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RsaKeyProperties prop,RedisTemplate redisTemplate,UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.prop = prop;
        this.redisTemplate = redisTemplate;
        this.userMapper = userMapper;
    }

    //接收并解析用户凭证，出現错误时，返回json数据前端
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //将json格式请求体转成JavaBean对象
            SysUser user = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            try {
                //如果认证失败，提供自定义json格式异常
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter out = response.getWriter();
                ResponseResult result = ResponseResult.error();
                out.write(new ObjectMapper().writeValueAsString(result));
                out.flush();
                out.close();
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        }
    }

    //用户登录成功后，生成token,并且返回json数据给前端
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //得到当前认证的用户对象
        SysUser user = userMapper.findByName(authResult.getName());
        user.setPassword(null); //删除密码
        //json web token构建
        String token = JwtUtils.generateTokenExpireInMinutes(user, prop.getPrivateKey(), 24 * 60);
//        //将token保存在header中返回token
//        response.addHeader("Authorization","Bearer "+token);

        //将token保存在redis中
        redisTemplate.opsForHash().put(user.getUsername(),"userToken",token);

        //登录成功后，返回json格式进行提示
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        ResponseResult result = ResponseResult.success();
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }
}
