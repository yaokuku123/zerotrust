package com.ustb.zerotrust.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.ustb.zerotrust.domain.DaemonSoft;
import com.ustb.zerotrust.mapper.FilePathStoreMapper;
import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.util.DockerClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileStoreServiceImpl
 * Author: yaoqijun
 * Date: 2021/6/25 10:35
 */
@Service
public class FileStoreServiceImpl implements FileStoreService {

    @Value("${docker.address}")
    private String dockerAddress;

    @Autowired
    private FilePathStoreMapper filePathStoreMapper;

    @Override
    public void uploadFile(String fileName, MultipartFile softFile, MultipartFile signFile) {
        //拼接路径
        String filePath = System.getProperty("user.dir") + "/daemonFile/";
        String softDestPath = filePath + softFile.getOriginalFilename();
        String signDestPath = filePath + signFile.getOriginalFilename();
        File fileDir = new File(filePath);
        //创建文件夹
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File softDestFile = new File(softDestPath);
        File signDestFile = new File(signDestPath);
        try {
            //将被测软件保存至本地
            softFile.transferTo(softDestFile);
            signFile.transferTo(signDestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将位置信息存储至数据库
        DaemonSoft daemonSoft = new DaemonSoft(fileName, softDestPath, signDestPath);
        filePathStoreMapper.insertFilePath(daemonSoft);
    }

    @Override
    public void imageBuild(String fileName) throws Exception {
        //获取软件路径
        DaemonSoft daemonSoft = filePathStoreMapper.getFilePath(fileName);
        String softPath = daemonSoft.getSoftPath();
        String filePath = System.getProperty("user.dir") + "/daemonFile/";
        //docker部署
        DockerClientUtils dockerClientUtils = new DockerClientUtils();
        //构建Dockerfile
        String softName = softPath.substring(softPath.lastIndexOf("/")+1);
        dockerClientUtils.buildDockerfile(softName);
        //构建镜像和容器
        DockerClient dockerClient = new DockerClientUtils().connectDocker(dockerAddress);
        dockerClientUtils.buildImage(dockerClient,filePath,9002,9002);
    }

}
