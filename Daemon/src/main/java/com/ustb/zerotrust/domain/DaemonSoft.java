package com.ustb.zerotrust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: DaemonSoft
 * Author: yaoqijun
 * Date: 2021/6/25 18:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaemonSoft {
    private String appName;
    private String softPath;
    private String signPath;
}
