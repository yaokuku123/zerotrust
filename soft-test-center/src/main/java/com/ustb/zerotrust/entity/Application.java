package com.ustb.zerotrust.entity;

public class Application {

    private String appName;
    private String filePath;
    private String signPath;

    public Application(String appName, String filePath, String signPath) {
        this.appName = appName;
        this.filePath = filePath;
        this.signPath = signPath;
    }

    public Application() {

    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSignPath() {
        return signPath;
    }

    public void setSignPath(String signPath) {
        this.signPath = signPath;
    }




}
