package com.ustb.zerotrust.service.impl;

import org.springframework.stereotype.Service;

@Service
public class CleanInfoVo {
    public boolean isSoftflag() {
        return softflag;
    }

    public void setSoftflag(boolean softflag) {
        this.softflag = softflag;
    }

    public boolean isCleanflag() {
        return cleanflag;
    }

    public void setCleanflag(boolean cleanflag) {
        this.cleanflag = cleanflag;
    }

    public String getObtxid() {
        return obtxid;
    }

    public void setObtxid(String obtxid) {
        this.obtxid = obtxid;
    }

    private boolean softflag;
    private boolean cleanflag;
    private String obtxid;
}
