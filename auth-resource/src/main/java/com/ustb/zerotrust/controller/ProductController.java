package com.ustb.zerotrust.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: ProductController
 * Author: yaoqijun
 * Date: 2021/2/25 11:20
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Secured("ROLE_PRODUCT")
    @GetMapping("/findAll")
    public String findAll(){
        return "9002 success findAll...";
    }
}
