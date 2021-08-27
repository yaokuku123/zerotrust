package com.ustb.zerotrust.service;

import com.ustb.zerotrust.domain.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "data-clean")
public interface DataCleanFeignClient {

    /**
     * 调用数据清理模块
     * @param fileName 软件名称
     * @return 是否清洗成功
     */
    @GetMapping("/ExtractData")
    public ResponseResult findExtractData(@RequestParam("fileName") String fileName);
}
