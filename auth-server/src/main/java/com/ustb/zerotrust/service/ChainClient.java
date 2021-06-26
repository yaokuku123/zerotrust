package com.ustb.zerotrust.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author WYP
 * @date 2021-06-26 14:34
 */
@FeignClient(name = "soft-test-center")
public interface ChainClient {

    /**
     * 从链中获取交易id 得到参数
     * @param  appName
     * @return 返回查询参数
     */

    @RequestMapping("/getParam")
    JSONObject getParam(@RequestParam("appName") String appName);
}
