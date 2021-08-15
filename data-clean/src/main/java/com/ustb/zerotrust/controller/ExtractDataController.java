package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.ExtractData;
import com.ustb.zerotrust.service.ExtractDataService;
import com.ustb.zerotrust.util.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author WYP
 * @date 2021-08-15 21:16
 */
@RestController
public class ExtractDataController {

    @Resource
    private ExtractDataService extractDataService;

    @GetMapping("/ExtractData")
    public ResponseResult findExtractData(){
        List<ExtractData> extractDataList = extractDataService.findAll();
        for (ExtractData extractData:extractDataList){
            //system.out.println(MD5Utils.code(extractData.getCloudIdCrypto()));
            extractData.setCloudIdCrypto(MD5Utils.generate(extractData.getCloudIdCrypto()));
            extractData.setIpCrypto(MD5Utils.generate(extractData.getIpCrypto()));
            extractData.setTaijiCloudId(extractData.getId());
            extractDataService.ExtractAsView(extractData);
        }
        return ResponseResult.success().data("ExtractData",extractDataList);
    }
}
