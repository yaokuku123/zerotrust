package com.ustb.zerotrust.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 区块链存储的内容
 * SoftInfo类为被测软件的详细信息，该类中的全部信息需要在区块链的交易中进行存储。当用户希望查询软件详细信息时，
 * 需要通过交易地址获取对应交易中存储的软件详细信息返回。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoftInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id; //id 主键
    private int softFileStoreId; // 被测软件保存路径的外键
    private String softName; //软件名称，（区块链存储）
    private String softDesc; //软件描述，（区块链存储）
    private String userName; //软件拥有者，（区块链存储）
    private String phoneNum; //软件拥有者手机号，（区块链存储）
    private Date createTime; //创建时间，（区块链存储）
    private String txId; //软件交易id
    private Integer status; // 状态，0表示软件正在被处理，1表示软件待审核，2表示软件已注册成功，3表示软件已驳回
    private Boolean isDeleted; //逻辑删除 1（true）已删除， 0（false）未删除
}
