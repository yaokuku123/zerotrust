package com.ustb.zerotrust;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ControlSystemApplication
 * Author: yaoqijun
 * Date: 2021/6/17 15:42
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ustb"})
@MapperScan("com.ustb.zerotrust.mapper")
@EnableFeignClients
public class ControlSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControlSystemApplication.class,args);
    }
}
