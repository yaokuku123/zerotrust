package com.ustb.zerotrust.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontController {

    @RequestMapping("/front")
    public String front() {
        return "front";
    }
}
