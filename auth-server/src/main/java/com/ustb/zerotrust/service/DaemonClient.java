package com.ustb.zerotrust.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author WYP
 * @date 2021-06-25 19:58
 */

@FeignClient(name = "Daemon")
public interface DaemonClient {

    /**
     * 查询守护进程获取参数
     * @param
     * @return 返回查询参数
     */

    @RequestMapping("/GetCheckMessage")
    public String getMessage();


    @RequestMapping("/hello2")
    public String Hello2();


}
