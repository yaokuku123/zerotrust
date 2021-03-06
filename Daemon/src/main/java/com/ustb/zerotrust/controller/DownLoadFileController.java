package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseCodeEnum;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: DownLoadFileController
 * Author: yaoqijun
 * Date: 2021/6/25 10:20
 */
@RestController
@RequestMapping("/file")
public class DownLoadFileController {

    @Autowired
    private FileStoreService fileStoreService; //保存文件服务

    /**
     * 接收软件测试中心发送的被测软件和签名文件
     *
     * @param fileName 软件名称
     * @param files    被测软件和签名文件
     * @return
     */
    @PostMapping(value = "/download", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult downLoad(@RequestParam("fileName") String fileName, @RequestParam("files") MultipartFile[] files) throws Exception {
        if (files.length != 2) {
            return ResponseResult.error().message("文件个数错误");
        }
        //下载文件至本地
        fileStoreService.uploadFile(fileName, files[0], files[1]);
        //构建镜像与容器部署
        fileStoreService.imageBuild(fileName);
        return ResponseResult.success();
    }
}
