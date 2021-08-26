package com.ustb.zerotrust.service;

import com.ustb.zerotrust.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author WYP
 * @date 2021-06-25 10:18
 */

@FeignClient(name = "auth-server")
public interface CheckClient {
    /**
     * 查询认证服务器
     * @param
     * @return 返回查询结果
     */
    @GetMapping("/check/result")
    public boolean checkResult(@RequestParam("fileName") String fileName);

    @GetMapping("/check/resultV2")
    public ResponseResult checkResultV2(@RequestParam("fileName") String fileName, @RequestParam("resource") String resource);
}
