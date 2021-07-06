package com.ustb.zerotrust;


import com.ustb.zerotrust.servicebase.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: GatewayApplication
 * Author: yaoqijun
 * Date: 2021/5/25 16:15
 */
@SpringBootApplication
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {SwaggerConfig.class})
})
@EnableFeignClients
//@EnableConfigurationProperties(RsaKeyProperties.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
