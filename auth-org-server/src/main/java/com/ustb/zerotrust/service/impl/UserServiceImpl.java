package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.domain.SysUser;
import com.ustb.zerotrust.mapper.UserMapper;
import com.ustb.zerotrust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: UserServiceImpl
 * Author: yaoqijun
 * Date: 2021/2/24 10:19
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 从数据库中获取用户数据
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.findByName(username);
        return User.withUsername(user.getUsername()).password(user.getPassword())
                .authorities((GrantedAuthority) () -> "GUEST").build();
    }
}
