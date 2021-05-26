package com.ustb.zerotrust.service;

import com.ustb.zerotrust.domain.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: UserService
 * Author: yaoqijun
 * Date: 2021/2/24 10:18
 */
public interface UserService extends UserDetailsService {

    //添加用户
    public Boolean addUser(SysUser sysUser);
}
