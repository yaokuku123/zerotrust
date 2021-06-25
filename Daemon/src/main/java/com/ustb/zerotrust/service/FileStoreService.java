package com.ustb.zerotrust.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileStoreService
 * Author: yaoqijun
 * Date: 2021/6/25 10:35
 */
public interface FileStoreService {

    /**
     * 保存被测软件
     *
     * @param fileName 上传文件的名称
     * @param softFile 被测软件
     * @param signFile 签名文件
     * @return 上传的被测软件路径
     */
    public void uploadFile(String fileName, MultipartFile softFile, MultipartFile signFile);
}
