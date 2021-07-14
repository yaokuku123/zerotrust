package com.ustb.zerotrust.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回详细的链上数据信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class TxInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String txId; //交易id
    private String toAddress; //链地址
    private String blockHash; //区块hash
    private String softName; //软件名称
    private String softDesc; //软件描述
    private String softSize; //软件大小
    private String softType; //软件类型
    private String userName; //软件拥有者
    private String phoneNum; //软件拥有者手机号
    private Date createTime; //上链时间
}
