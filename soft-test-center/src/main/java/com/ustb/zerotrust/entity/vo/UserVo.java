package com.ustb.zerotrust.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}
