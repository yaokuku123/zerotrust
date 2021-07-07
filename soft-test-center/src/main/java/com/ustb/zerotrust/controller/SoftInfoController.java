package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/soft")
public class SoftInfoController {

    /**
     * 获取当前系统中存在的软件交易信息
     * @return 交易信息列表
     */
    @GetMapping("/list")
    public ResponseResult getSoftList(){
        //TODO 从数据库中获取当前的软件交易信息
        return ResponseResult.success();
    }

    /**
     * 第一步，添加被测软件的相关信息资料
     * @param softSimpleInfoVo 前端填写的软件信息
     * @return 被测对象的id主键
     */
    @PostMapping("/add")
    public ResponseResult addSoft(@RequestBody SoftSimpleInfoVo softSimpleInfoVo){
        //TODO 添加软件的信息到SoftInfo对象中，并返回该对象的主键ID给前端
        return ResponseResult.success();
    }

    /**
     * 第二步，上传被测软件文件
     * @param softId 软件信息id
     * @param file 被测软件文件
     * @return 无
     */
    @PostMapping("/upload/{id}")
    public ResponseResult softUpload(@PathVariable("id") String softId, @RequestParam("file") MultipartFile file){
        //TODO 上传被测软件到测试中心本地，并将对应的软件信息softId主键以及软件存储的路径封装至SoftFileStore对象中
        //TODO 另外，将SoftFileStore对象返回的主键id保存至对应的软件信息对象SoftInfo的softFileStoreId属性中
        return ResponseResult.success();
    }

    /**
     * 第三步，审核当前被测软件，将当前软件的状态修改为待审核状态，等待审核即可
     * @param id 软件信息主键id
     * @return 无
     */
    @PostMapping("/verity/{id}")
    public ResponseResult softVerify(@PathVariable("id") String id){
        //TODO 修改SoftInfo对象的状态为：1软件待审核
        return ResponseResult.success();
    }
}
