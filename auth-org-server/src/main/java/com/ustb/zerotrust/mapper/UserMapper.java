package com.ustb.zerotrust.mapper;


import com.ustb.zerotrust.domain.SysUser;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: UserMapper
 * Author: yaoqijun
 * Date: 2021/2/24 10:17
 */
public interface UserMapper {

    //根据用户名查询
    public SysUser findByName(String username);

    //添加用户信息
    public Integer addUser(SysUser sysUser);
}
