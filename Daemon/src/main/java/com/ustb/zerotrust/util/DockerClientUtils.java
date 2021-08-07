package com.ustb.zerotrust.util;

import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;



/*
# 开放docker的对外服务端口
vim /lib/systemd/system/docker.service

ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock

systemctl daemon-reload
systemctl restart docker
 */

public class DockerClientUtils {
    /**
     * 连接Docker服务器
     * @return
     */
    public DockerClient connectDocker(String dockerInstance){
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerInstance).build();
        dockerClient.infoCmd().exec();
        return dockerClient;
    }

    /**
     * 创建容器
     * @param client
     * @return
     */
    public CreateContainerResponse createContainers(DockerClient client, String containerName, String imageName){
        CreateContainerResponse container = client.createContainerCmd(imageName)
                .withName(containerName)
                .exec();
        return container;
    }

    /**
     * 启动容器
     * @param client
     * @param containerId
     */
    public void startContainer(DockerClient client,String containerId){
        client.startContainerCmd(containerId).exec();
    }

    /**
     * 启动容器
     * @param client
     * @param containerId
     */
    public void stopContainer(DockerClient client,String containerId){
        client.stopContainerCmd(containerId).exec();
    }

    /**
     * 删除容器
     * @param client
     * @param containerId
     */
    public void removeContainer(DockerClient client,String containerId){
        client.removeContainerCmd(containerId).exec();

    }

    public static void main(String[] args){
        DockerClientUtils dockerClientUtils =new DockerClientUtils();
        //连接Docker服务器
        DockerClient client = dockerClientUtils.connectDocker("tcp://123.56.246.148:2375");
        //创建容器
        CreateContainerResponse container = dockerClientUtils.createContainers(client,"sny_hello","hello-world");
        //启动容器
        dockerClientUtils.startContainer(client,container.getId());
    }

}