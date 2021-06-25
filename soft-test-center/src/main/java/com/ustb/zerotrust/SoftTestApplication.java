package com.ustb.zerotrust;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: SoftTestApplication
 * Author: yaoqijun
 * Date: 2021/6/15 9:16
 */
@SpringBootApplication
@MapperScan("com.ustb.zerotrust.mapper")
@EnableFeignClients
public class SoftTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoftTestApplication.class,args);
    }
}
