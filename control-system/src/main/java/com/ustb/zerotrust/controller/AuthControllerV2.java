package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.RequestGrant;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.service.CheckClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: AuthController
 * Author: yaoqijun
 * Date: 2021/6/17 15:46
 */
@RestController
@RequestMapping("/auth")
public class AuthControllerV2 {

    @Autowired
    private CheckClient checkClient;

    /**
     * 接收网关的检验请求
     * @param requestGrant 用户请求信息
     * @return 是否通过
     */
    @PostMapping("/verify")
    public ResponseResult Verify(@RequestBody RequestGrant requestGrant){
        //System.out.println(requestGrant);
        // 根据用户请求信息，监管平台进行验证处理
        boolean flag = checkClient.checkResultV2(requestGrant.getSoftName());

        return ResponseResult.success().data("flag",flag);
    }
}
