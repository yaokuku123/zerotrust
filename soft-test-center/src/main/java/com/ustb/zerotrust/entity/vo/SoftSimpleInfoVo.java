package com.ustb.zerotrust.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接收用户前端发送的软件相关注册信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoftSimpleInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getSoftDesc() {
        return softDesc;
    }

    public void setSoftDesc(String softDesc) {
        this.softDesc = softDesc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    private String softName; //软件名称
    private String softDesc; //软件描述
    private String userName; //软件拥有者
    private String phoneNum; //软件拥有者手机号

}
