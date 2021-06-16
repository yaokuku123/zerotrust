package com.ustb.zerotrust.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileUploadService
 * Author: yaoqijun
 * Date: 2021/6/15 9:36
 */
public interface FileStoreService {
    /**
     * 保存被测软件
     *
     * @param file 上传文件
     * @return 上传的被测软件路径
     */
    public String uploadFile(MultipartFile file);

    /**
     * 保存被测软件的签名文件
     *
     * @param signList 签名列表字符串
     * @param softName 被测软件名称
     * @return 签名文件路径
     */
    public String uploadFileSign(String softName, String signList);
}
