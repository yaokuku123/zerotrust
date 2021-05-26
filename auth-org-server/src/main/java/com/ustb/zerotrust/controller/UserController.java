package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseCodeEnum;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.domain.SysUser;
import com.ustb.zerotrust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: UserController
 * Author: yaoqijun
 * Date: 2021/5/26 10:21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
    @PostMapping("/add")
    public ResponseResult addUser(@RequestBody SysUser sysUser){
        Boolean flag = userService.addUser(sysUser);
        if (flag){
            return ResponseResult.success();
        }
        return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(),ResponseCodeEnum.FAIL.getMessage());
    }
}
