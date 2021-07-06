package com.ustb.zerotrust.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据库存储信息
 * SoftTxInfo类，存储各个被测软件对应的区块链交易id信息，前端查询已注册软件列表时返回该类的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftTxInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; //id 主键
    private String softName; //软件名称
    private String txId; //软件交易id
    private Integer status; // 状态，0表示软件正在被处理，1表示软件待审核，2表示软件已注册成功，3表示软件已驳回
    private Boolean isDeleted; //逻辑删除 1（true）已删除， 0（false）未删除
}
