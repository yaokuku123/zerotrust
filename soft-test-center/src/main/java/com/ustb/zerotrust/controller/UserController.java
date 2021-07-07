package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.vo.UserVo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    //login
    @PostMapping("login")
    public ResponseResult login(@RequestBody UserVo userVo) {
        System.out.println(userVo);
        return ResponseResult.success().data("token","admin");
    }

    //info
    @GetMapping("info")
    public ResponseResult info(String token) {
        System.out.println(token);
        return ResponseResult.success().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
