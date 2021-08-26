package com.ustb.zerotrust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean softflag;
    private boolean cleanflag;
    private String obtxid;
}
