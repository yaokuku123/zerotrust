package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
public class FrontController {

    @PostMapping("/front")
    @ResponseBody
    public ResponseResult front() {
        return ResponseResult.success();
    }

    @GetMapping("/front-get")
    public String frontGet() {
        return "front";
    }
}
