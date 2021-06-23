package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseCodeEnum;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.service.FileSignService;
import com.ustb.zerotrust.service.FileStoreService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;


/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileUploadController
 * Author: yaoqijun
 * Date: 2021/6/15 9:20
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileStoreService fileStoreService; //上传文件服务

    @Autowired
    private FileSignService fileSignService; //签名服务


    /**
     * 将被测软件上传至软件测评中心，用于检测软件合法性
     *
     * @param file 上传的被测软件
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file) throws ClassNotFoundException, ShellChainException, SQLException, UnsupportedEncodingException, FileNotFoundException {
        //边界判定
        if (file.isEmpty()) {
            return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(), "上传失败，请选择文件");
        }
        if (file.getSize() < 1024) {
            return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(), "上传失败，请选择大于1KB文件");
        }
        //上传软件
        String filePath = fileStoreService.uploadFile(file);
        if (filePath == null) {
            //上传文件失败
            return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(), "上传失败，文件IO异常");
        }
        //签名软件
        if (!fileSignService.signFile(filePath)) {
            //签名失败
            return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(), "上传失败，软件检验未通过");
        }
        //验证通过
        return ResponseResult.success();
    }

}
