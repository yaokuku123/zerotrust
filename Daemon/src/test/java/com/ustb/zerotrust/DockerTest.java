package com.ustb.zerotrust;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import com.ustb.zerotrust.util.DockerClientUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DockerTest {

    private DockerClient dockerClient = new DockerClientUtils().connectDocker("tcp://123.56.246.148:2375");


    @Test
    public void buildImageFromTar() throws Exception {
        File baseDir = new File("/Users/yorick/Documents/work/project/zerotrust/daemonFile/JAVA");
        Collection<File> files = FileUtils.listFiles(baseDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        File tarFile = CompressArchiveUtil.archiveTARFiles(baseDir, files, UUID.randomUUID().toString());
        String response = dockerfileBuild(new FileInputStream(tarFile));
        System.out.println(response);
    }

    private String dockerfileBuild(InputStream tarInputStream) throws Exception {

        return execBuild(dockerClient.buildImageCmd().withTarInputStream(tarInputStream));
    }

    private String execBuild(BuildImageCmd buildImageCmd) throws Exception {
        String imageId = buildImageCmd.withNoCache(true)
                .withTag("docker-java-onbuild")
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        // Create container based on image
        ExposedPort tcp22 = ExposedPort.tcp(9002);
        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Ports.Binding.bindPort(9002));
        CreateContainerResponse container = dockerClient.createContainerCmd(imageId).withExposedPorts(tcp22).withPortBindings(portBindings).exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        //dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback()).awaitStatusCode();

        return containerLog(container.getId());
    }

    private String containerLog(String containerId) throws Exception {
        return dockerClient.logContainerCmd(containerId).withStdOut(true).exec(new LogContainerTestCallback())
                .awaitCompletion().toString();
    }

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

    @Test
    public void testDockerUtilsImageBuild() throws Exception {
        DockerClientUtils dockerClientUtils = new DockerClientUtils();
        dockerClientUtils.buildImage(dockerClient,"/Users/yorick/Documents/work/project/zerotrust/daemonFile/JAVA",9002,9002);
    }

}
