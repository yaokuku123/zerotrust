package com.ustb.zerotrust.service;

import com.ustb.zerotrust.config.FeignMultipartSupportConfig;
import com.ustb.zerotrust.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileTransFeignClient
 * Author: yaoqijun
 * Date: 2021/6/25 18:33
 */
@FeignClient(name = "daemon", configuration = FeignMultipartSupportConfig.class)
public interface FileTransFeignClient {
    /**
     * 接收软件测试中心发送的被测软件和签名文件
     *
     * @param fileName 软件名称
     * @param files    被测软件和签名文件
     * @return
     */
    @PostMapping(value = "/file/download", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult downLoad(@RequestParam("fileName") String fileName,
                                   @RequestPart("files") MultipartFile[] files);
}
