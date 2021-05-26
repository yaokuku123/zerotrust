package com.ustb.zerotrust.mapper;


import com.ustb.zerotrust.domain.SysUser;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: UserMapper
 * Author: yaoqijun
 * Date: 2021/2/24 10:17
 */
public interface UserMapper {

    @Select("select * from user where username=#{username}")
    public SysUser findByName(String username);
}
