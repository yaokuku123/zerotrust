package com.ustb.zerotrust.mapper;


import com.ustb.zerotrust.domain.SysRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: RoleMapper
 * Author: yaoqijun
 * Date: 2021/2/24 10:18
 */
public interface RoleMapper {

    @Select(" select r.id,r.role_name roleName,r.role_desc roleDesc " +
            " from sys_role r,sys_user_role ur " +
            " where r.id=ur.rid and ur.uid=#{uid} ")
    public List<SysRole> findByUid(Integer uid);
}
