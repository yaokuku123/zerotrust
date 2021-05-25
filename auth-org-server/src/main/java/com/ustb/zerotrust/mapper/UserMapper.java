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

    @Select("select * from sys_user where username=#{username}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roles", column = "id", javaType = List.class,
                    many = @Many(select = "com.ustb.zerotrust.mapper.RoleMapper.findByUid"))
    })
    public SysUser findByName(String username);
}
