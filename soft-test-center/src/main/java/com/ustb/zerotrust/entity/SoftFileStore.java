package com.ustb.zerotrust.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据库存储信息
 * SoftFileStore类，用于保存上传被测软件的存储位置，用于在审核时，返回给审核人员进行审核
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoftFileStore implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id; //id 主键

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoftInfoId() {
        return softInfoId;
    }

    public void setSoftInfoId(int softInfoId) {
        this.softInfoId = softInfoId;
    }

    public String getSoftPath() {
        return softPath;
    }

    public void setSoftPath(String softPath) {
        this.softPath = softPath;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    private Integer softInfoId; //被测软件信息的外键
    private String softPath; //被测软件存储路径
    private Boolean isDeleted; //逻辑删除 1（true）已删除， 0（false）未删除
}
