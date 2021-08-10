package com.ustb.zerotrust.util;

import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;



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
     * 停止容器
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

    /**
     * 日志记录类
     */
    public static class LogContainerTestCallback extends LogContainerResultCallback {
        protected final StringBuffer log = new StringBuffer();

        List<Frame> collectedFrames = new ArrayList<Frame>();

        boolean collectFrames = false;

        public LogContainerTestCallback() {
            this(false);
        }

        public LogContainerTestCallback(boolean collectFrames) {
            this.collectFrames = collectFrames;
        }

        @Override
        public void onNext(Frame frame) {
            if(collectFrames) collectedFrames.add(frame);
            log.append(new String(frame.getPayload()));
        }

        @Override
        public String toString() {
            return log.toString();
        }


        public List<Frame> getCollectedFrames() {
            return collectedFrames;
        }
    }

    private String containerLog(DockerClient dockerClient,String containerId) throws Exception {
        return dockerClient.logContainerCmd(containerId).withStdOut(true).exec(new LogContainerTestCallback())
                .awaitCompletion().toString();
    }

    /**
     * 具体执行构建镜像并启动镜像方法
     * @param dockerClient
     * @param tarInputStream
     * @param exposePort
     * @param bindPort
     * @return
     * @throws Exception
     */
    private String dockerfileBuild(DockerClient dockerClient,InputStream tarInputStream,int exposePort,int bindPort) throws Exception {
        String imageId = dockerClient.buildImageCmd().withTarInputStream(tarInputStream)
                .withNoCache(true)
                //.withTag("docker-java-onbuild")
                .exec(new BuildImageResultCallback())
                .awaitImageId();
        // Create container based on image
        ExposedPort tcp22 = ExposedPort.tcp(exposePort);
        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Ports.Binding.bindPort(bindPort));
        CreateContainerResponse container = dockerClient.createContainerCmd(imageId)
                .withExposedPorts(tcp22)
                .withPortBindings(portBindings)
                .exec();
        startContainer(dockerClient,container.getId());
        return containerLog(dockerClient,container.getId());
    }

    /**
     * 构建镜像并启动容器
     * @param dockerClient
     * @param dockerFilePath dockerfile文件所在目录路径
     * @param exposePort 暴露端口
     * @param bindPort 绑定端口
     * @throws Exception
     */
    public void buildImage(DockerClient dockerClient,String dockerFilePath,int exposePort,int bindPort) throws Exception{
        File baseDir = new File(dockerFilePath);
        Collection<File> files = FileUtils.listFiles(baseDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        File tarFile = CompressArchiveUtil.archiveTARFiles(baseDir, files, UUID.randomUUID().toString());
        String response = dockerfileBuild(dockerClient,new FileInputStream(tarFile),exposePort,bindPort);
        //System.out.println(response);
    }

    /**
     * 构建Dockerfile
     * @param softName
     */
    public void buildDockerfile(String softName){
        String filePath = System.getProperty("user.dir") + "/daemonFile/";
        String dockerImage = "FROM java:8 \nMAINTAINER yqj<yaoqijun@outlook.com> \n" +
                "COPY "+softName+" /app.jar\nENTRYPOINT [\"java\",\"-jar\",\"/app.jar\"]";
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath+"Dockerfile"));
            bw.write(dockerImage);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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