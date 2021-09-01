package com.ustb.zerotrust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractData implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; //主键
    private String govName; //政府机构名称
    private String comName; //使用单位名称
    private String monitorTime; //监控日期时间
    private String serviceName; //业务系统名称
    private String cloudId; //云主机ID(加密)
    private String virtualName; //虚拟机名称
    private String ip; //浮动IP/弹性IP(加密)
    private Integer taijiCloudId; //taiji_cloud表主键id
}
