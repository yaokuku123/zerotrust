package com.ustb.zerotrust.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: SysRole
 * Author: yaoqijun
 * Date: 2021/2/24 10:15
 */
@Data
public class SysRole implements GrantedAuthority {

    private Integer id;
    private String roleName;
    private String roleDesc;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
