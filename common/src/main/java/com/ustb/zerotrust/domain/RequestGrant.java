package com.ustb.zerotrust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: RequestGrant
 * Author: yaoqijun
 * Date: 2021/6/17 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestGrant implements Serializable {
    private static final long serialVersionUID = 1L;
    private String softName;
    private String action;
    private String useTime;
    private String userName;
}
