package com.ustb.zerotrust;

import com.ustb.zerotrust.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: GatewayApplication
 * Author: yaoqijun
 * Date: 2021/5/25 16:15
 */
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
