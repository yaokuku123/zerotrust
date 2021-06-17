package com.ustb.zerotrust.service;

import com.ustb.zerotrust.domain.RequestGrant;
import com.ustb.zerotrust.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: GrantFeignClient
 * Author: yaoqijun
 * Date: 2021/6/17 19:07
 */
@FeignClient(name = "control-system")
public interface GrantFeignClient {

    /**
     * 接收网关的检验请求
     * @param requestGrant 用户请求信息
     * @return 是否通过
     */
    @PostMapping("/auth/verify")
    public ResponseResult Verify(@RequestBody RequestGrant requestGrant);
}
