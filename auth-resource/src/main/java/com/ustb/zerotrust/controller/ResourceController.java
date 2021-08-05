package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ExtractData;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.domain.vo.ExtractDataVo;
import com.ustb.zerotrust.service.ExtractDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ExtractDataService extractDataService;

    /**
     * 根据id获取数据字段
     */
    @GetMapping("/get/{id}")
    public ResponseResult getDataById(@PathVariable("id") int id){
        ExtractData extractData = extractDataService.selectDataById(id);
        return ResponseResult.success().data("extractData",extractData);
    }

    /**
     * 更新数据字段
     */
    @PostMapping("/update")
    public ResponseResult updateData(@RequestBody ExtractDataVo extractDataVo){
        ExtractData extractData = extractDataService.selectDataById(extractDataVo.getId());
        BeanUtils.copyProperties(extractDataVo,extractData);
        if (extractDataService.updateData(extractData)){
            return ResponseResult.success();
        }
        return ResponseResult.error();
    }

    /**
     * 删除数据字段
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteData(@PathVariable("id") int id){
        if (extractDataService.deleteData(id)){
            return ResponseResult.success();
        }
        return ResponseResult.error();
    }

}
