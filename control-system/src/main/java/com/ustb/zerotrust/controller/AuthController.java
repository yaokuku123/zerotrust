package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.RequestGrant;
import com.ustb.zerotrust.domain.ResponseResult;
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
public class AuthController {

    /**
     * 接收网关的检验请求
     * @param requestGrant 用户请求信息
     * @return 是否通过
     */
    @PostMapping("/verify")
    public ResponseResult Verify(@RequestBody RequestGrant requestGrant){
        System.out.println(requestGrant);
        //TODO 根据用户请求信息，监管平台进行验证处理
        return ResponseResult.success(requestGrant);
    }
}
