package com.ustb.zerotrust.test;

import com.ustb.zerotrust.utils.RsaUtils;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: RsaUtilsTest
 * Author: yaoqijun
 * Date: 2021/2/25 9:34
 * 测试：使用RSA工具生成公私钥
 */

public class RsaUtilsTest {

    private String privateKeyPath = "D:\\temp\\auth_key";
    private String publicKeyPath = "D:\\temp\\auth_key.pub";

    @Test
    public void generateKey() throws Exception {
        RsaUtils.generateKey(publicKeyPath,privateKeyPath,"yorick",2048);
    }

    @Test
    public void getKey() throws Exception {
        PublicKey publicKey = RsaUtils.getPublicKey(publicKeyPath);
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        System.out.println("publicKey: "+publicKey.toString());
        System.out.println("privateKey: "+privateKey.toString());
    }
}
