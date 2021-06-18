package com.ustb.zerotrust.config;

import com.ustb.zerotrust.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: RsaKeyProperties
 * Author: yaoqijun
 * Date: 2021/2/25 11:04
 */
//@Data
//@ConfigurationProperties("ustb.key")
//public class RsaKeyProperties {
//    private String publicKeyPath;
//    private PublicKey publicKey;
//
//    @PostConstruct
//    public void loadKey() throws Exception {
//        publicKey = RsaUtils.getPublicKey(publicKeyPath);
//    }
//}
