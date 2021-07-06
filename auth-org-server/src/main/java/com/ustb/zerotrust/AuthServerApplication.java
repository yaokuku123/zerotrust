package com.ustb.zerotrust;


import com.ustb.zerotrust.config.RsaKeyProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: AuthServerApplication
 * Author: yaoqijun
 * Date: 2021/2/25 9:50
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ustb"})
@MapperScan("com.ustb.zerotrust.mapper")
@EnableConfigurationProperties(RsaKeyProperties.class)
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class,args);
    }
}
