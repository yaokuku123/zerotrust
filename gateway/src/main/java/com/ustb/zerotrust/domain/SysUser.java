package com.ustb.zerotrust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: SysUser
 * Author: yaoqijun
 * Date: 2021/2/24 10:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {

    private Integer id;
    private String username;
    private String password;
    private String cardId;
    private Integer gender;

}
