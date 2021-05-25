package com.ustb.zerotrust.config;


import com.ustb.zerotrust.filter.TokenLoginFilter;
import com.ustb.zerotrust.filter.TokenVerifyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: WebSecurityConfig
 * Author: yaoqijun
 * Date: 2021/2/25 10:33
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RsaKeyProperties prop;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭跨站请求防护
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                //允许不登陆就可以访问的方法，多个用逗号分隔
                .antMatchers("/product").hasAnyRole("USER")
                //其他的需要授权后访问
                .anyRequest().authenticated()
                .and()
                //增加自定义认证过滤器
                .addFilter(new TokenLoginFilter(authenticationManager(),prop))
                //增加自定义验证认证过滤器
                .addFilter(new TokenVerifyFilter(authenticationManager(),prop))
                //前后端分离是无状态的，不用session了，直接禁用。
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
