package com.ustb.zerotrust.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractDataVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; //主键
    private String govName; //政府机构名称
    private String comName; //使用单位名称
    private String monitorTime; //监控日期时间
    private String serviceName; //业务系统名称
    private String virtualName; //虚拟机名称
}
