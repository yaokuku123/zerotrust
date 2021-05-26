package com.ustb.zerotrust.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ProductController
 * Author: yaoqijun
 * Date: 2021/2/25 11:20
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/findAll")
    public String findAll(HttpServletRequest request) {
        //从Header中获取gateway中传递过来的参数
        String username = request.getHeader("username");
        String cardId = request.getHeader("cardId");
        String gender = request.getHeader("gender");
        return "9002 success findAll... username:" + username + " cardId:" + cardId + " gender:" + gender;
    }
}
