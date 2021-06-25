package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseCodeEnum;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.service.FileSignService;
import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.service.FileTransFeignClient;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

    @Autowired
    private FileTransFeignClient fileTransFeignClient; //远程发送文件服务


    /**
     * 将被测软件上传至软件测评中心，用于检测软件合法性
     *
     * @param file 上传的被测软件
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file) throws ClassNotFoundException, ShellChainException, SQLException, IOException {
        //边界判定
        if (file.isEmpty()) {
            return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(), "上传失败，请选择文件");
        }
        if (file.getSize() < 1024) {
            return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(), "上传失败，请选择大于1KB文件");
        }
        //上传软件
        String originFileName = file.getOriginalFilename();
        String fileName = originFileName.substring(0, originFileName.lastIndexOf("."));
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));
        File softFile = fileStoreService.uploadFile(fileName, suffix, file);
        if (softFile == null) {
            //上传文件失败
            return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(), "上传失败，文件IO异常");
        }
        //签名软件
        File signFile = fileSignService.signFile(fileName, softFile.getAbsolutePath());
        if (signFile == null) {
            //签名失败
            return ResponseResult.error(ResponseCodeEnum.FAIL.getCode(), "上传失败，软件检验未通过");
        }
        //将被测软件和签名文件打包发送至目标虚拟机
        MultipartFile[] files = new MultipartFile[2];
        files[0] = new MockMultipartFile("files", softFile.getName(), ContentType.TEXT_PLAIN.toString(), new FileInputStream(softFile));
        files[1] = new MockMultipartFile("files", signFile.getName(), ContentType.TEXT_PLAIN.toString(), new FileInputStream(signFile));
        fileTransFeignClient.downLoad(fileName, files);

        //验证通过
        return ResponseResult.success();
    }

}
