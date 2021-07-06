package com.ustb.zerotrust;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: AuthProductApplication
 * Author: yaoqijun
 * Date: 2021/2/25 11:05
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ustb"})
@MapperScan("com.ustb.zerotrust.mapper")
public class AuthResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthResourceApplication.class,args);
    }
}
