package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.vo.QueryParamString;
import com.ustb.zerotrust.service.FileGetMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class FileControl {

    @Autowired
    private FileGetMessage fileGetMessage;

    @GetMapping("/GetCheckMessage")
    public QueryParamString GetMessage(String fileName) {
        QueryParamString checkMessage = fileGetMessage.getCheckMessage(fileName);
        return checkMessage;
    }

}
