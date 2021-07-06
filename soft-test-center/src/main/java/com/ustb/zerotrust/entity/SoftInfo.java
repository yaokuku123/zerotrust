package com.ustb.zerotrust.entity;

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

    private String id; //id 主键
    private String softName; //软件名称
    private String softDesc; //软件描述
    private String softSize; //软件大小
    private String softType; //软件类型
    private String userName; //软件拥有者
    private String phoneNum; //软件拥有者手机号
    private Date createTime; //上链时间
}
