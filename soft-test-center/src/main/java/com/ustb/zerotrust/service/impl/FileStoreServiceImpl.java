package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.utils.ConvertUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * 保存被测软件
     *
     * @param fileName 上传文件的名称
     * @param suffix 文件后缀
     * @param file 被测软件
     * @return 上传的被测软件路径
     */
    public File  uploadFile(String fileName,String suffix,MultipartFile file) {
        //拼接文件名，添加uuid
        fileName = fileName + UUID.randomUUID().toString().replaceAll("-","");
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
            return destFile;
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
    public File uploadFileSign(String softName, List<String> signList) {
        //拼接签名文件的名称
        String signFileName = softName + ".sign";
        //保存签名文件
        String destPath = System.getProperty("user.dir") + "/uploadFile/"+ signFileName;

        Map<String,Object> map = new HashMap<>();
        map.put("signStringList",signList);
        ConvertUtil.write2JsonFile(map,destPath);
        return new File(destPath);
    }
}
