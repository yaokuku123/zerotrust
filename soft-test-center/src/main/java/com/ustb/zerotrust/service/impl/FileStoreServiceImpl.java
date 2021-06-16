package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.service.FileStoreService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileUploadServiceImpl
 * Author: yaoqijun
 * Date: 2021/6/15 10:09
 */
@Service
public class FileStoreServiceImpl implements FileStoreService {
    /**
     * 上传被测软件
     *
     * @param file 上传的软件
     * @return 文件存储路径
     */
    public String uploadFile(MultipartFile file) {
        //拼接文件名，添加uuid
        String originFileName = file.getOriginalFilename();
        String fileName = originFileName.substring(0,originFileName.lastIndexOf("."))
                                + UUID.randomUUID().toString().replaceAll("-","");
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));
        fileName = fileName + suffix;
        //拼接路径
        String filePath = System.getProperty("user.dir") + "/uploadFile/";
        String destPath = filePath + fileName;
        File fileDir = new File(filePath);
        //创建文件夹
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File destFile = new File(destPath);
        try {
            //将被测软件保存至本地
            file.transferTo(destFile);
            return destPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存被测软件的签名文件
     *
     * @param signList 签名列表字符串
     * @param softName 被测软件名称
     * @return 签名文件路径
     */
    @Override
    public String uploadFileSign(String softName,String signList) {
        //拼接签名文件的名称
        String signFileName = softName + ".sign";
        //保存签名文件
        String destPath = System.getProperty("user.dir") + "/uploadFile/"+ signFileName;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(destPath));
            bw.write(signList);
            bw.flush();
            return destPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bw !=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
