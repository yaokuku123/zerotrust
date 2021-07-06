package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.SoftFile;
import com.ustb.zerotrust.service.SoftFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WYP
 * @date 2021-07-06 14:54
 */
@RestController
@CrossOrigin
public class FileController {

    @Autowired
    private SoftFileService softFileService;

    @RequestMapping("/findAll")
    public ResponseResult<List> findAll(){
        //ResponseResult<List> responseResult = new ResponseResult<>();
        List<SoftFile> softFiles = softFileService.findAll();
        return ResponseResult.success(softFiles);

    }


}
