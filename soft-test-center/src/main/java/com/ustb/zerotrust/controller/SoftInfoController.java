package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.service.SoftFileStoreService;
import com.ustb.zerotrust.service.SoftInfoService;
import com.ustb.zerotrust.service.SoftReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ustb.zerotrust.entity.SoftFileStore;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class SoftInfoController {
    private LinkDataBase linkDataBase = new LinkDataBase();
    private SoftReviewService softReviewService;

    @Autowired
    private FileStoreService fileStoreService; //上传文件服务

    @Autowired
    private SoftInfoService softInfoService;

    @Autowired
    private SoftFileStoreService softFileStoreService;

    /**
     * 获取当前系统中存在的软件交易信息
     * @return 交易信息列表
     */
    @GetMapping("/list")
    public ResponseResult getSoftList() throws SQLException, ClassNotFoundException {
        List<SoftInfo> softInfo = softInfoService.getSoftMessage();
        return ResponseResult.success().data("softInfo",softInfo);
    }

    /**
     * 第一步，添加被测软件的相关信息资料
     * @param softSimpleInfoVo 前端填写的软件信息
     * @return 被测对象的id主键
     */
    @PostMapping("/add")
    public ResponseResult addSoft(@RequestBody SoftSimpleInfoVo softSimpleInfoVo){
        SoftInfo softInfo = new SoftInfo();
        BeanUtils.copyProperties(softSimpleInfoVo,softInfo);
        softInfo.setCreateTime(new Date());
        softInfoService.insertSoft(softInfo);
        return ResponseResult.success().data("id",softInfo.getId());
    }

    /**
     * 根据主键id获取软件信息
     * @param id 主键id
     * @return 软件简要信息
     */
    @GetMapping("/get/{id}")
    public ResponseResult getSoft(@PathVariable("id") int id){
        SoftInfo softInfo = softInfoService.getSoft(id);
        SoftSimpleInfoVo softSimpleInfoVo = new SoftSimpleInfoVo();
        BeanUtils.copyProperties(softInfo,softSimpleInfoVo);
        return ResponseResult.success().data("softInfo",softSimpleInfoVo);
    }

    /**
     * 修改被测软件的相关信息
     * @param id 软件信息主键id
     * @param softSimpleInfoVo 前端回显后修改的软件信息
     * @return 无
     */
    @PostMapping("/update/{id}")
    public ResponseResult updateSoft(@PathVariable("id") int id,
                                         @RequestBody SoftSimpleInfoVo softSimpleInfoVo){
        SoftInfo softInfo = softInfoService.getSoft(id);
        softInfo.setSoftName(softSimpleInfoVo.getSoftName());
        softInfo.setSoftDesc(softSimpleInfoVo.getSoftDesc());
        softInfo.setUserName(softSimpleInfoVo.getUserName());
        softInfo.setPhoneNum(softSimpleInfoVo.getPhoneNum());
        softInfoService.updateSoft(softInfo);
        return ResponseResult.success();
    }

    /**
     * 第二步，上传被测软件文件
     * @param softId 软件信息id
     * @param file 被测软件文件
     * @return 无
     */
    @PostMapping("/upload/{id}")
    public ResponseResult softUpload(@PathVariable("id") int softId, @RequestParam("file") MultipartFile file){
        //TODO 上传被测软件到测试中心本地，并将对应的软件信息softId主键以及软件存储的路径封装至SoftFileStore对象中
        if (file.isEmpty()) {
            return ResponseResult.error().message("上传失败，请选择文件");
        }
        //上传软件
        String originFileName = file.getOriginalFilename();
        String fileName = originFileName.substring(0, originFileName.lastIndexOf("."));
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));
        File softFile = fileStoreService.uploadFile(fileName, suffix, file);
        if (softFile == null) {
            //上传文件失败
            return ResponseResult.error().message("上传失败，文件IO异常");
        }
        SoftFileStore softFileStore = new SoftFileStore();
        softFileStore.setSoftInfoId(softId);
        softFileStore.setSoftPath(softFile.getPath());
        System.out.println(softFileStore);
        //TODO 另外，将SoftFileStore对象返回的主键id保存至对应的软件信息对象SoftInfo的softFileStoreId属性中
        softFileStoreService.insertSoftFile(softFileStore);
        SoftInfo softInfo = softInfoService.getSoft(softId);
        softInfo.setSoftFileStoreId(softFileStore.getId());
        softInfoService.updateSoftFileId(softInfo);
        return ResponseResult.success();
    }

    /**
     * 第三步，审核当前被测软件，将当前软件的状态修改为待审核状态，等待审核即可
     * @param id 软件信息主键id
     * @return 无
     */
    @PostMapping("/verity/{id}")
    public ResponseResult softVerify(@PathVariable("id") int id) throws SQLException, ClassNotFoundException {
        SoftInfo softInfo = softInfoService.getSoft(id);
        softInfo.setStatus(1);//待审核状态
        softInfoService.verifySoft(softInfo);
        return ResponseResult.success();
    }
}
