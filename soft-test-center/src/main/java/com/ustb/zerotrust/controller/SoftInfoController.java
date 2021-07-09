package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.service.SoftReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ustb.zerotrust.entity.SoftFileStore;

import java.io.File;
import java.sql.SQLException;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class SoftInfoController {
    private LinkDataBase linkDataBase = new LinkDataBase();
    private SoftReviewService softReviewService;
    private SoftInfo softInfo =new SoftInfo();
    private SoftFileStore softFileStore =new SoftFileStore();
    @Autowired
    private FileStoreService fileStoreService; //上传文件服务
    /**
     * 获取当前系统中存在的软件交易信息
     * @return 交易信息列表
     */
    @GetMapping("/list")
    public ResponseResult getSoftList() throws SQLException, ClassNotFoundException {
        //TODO 从数据库中获取当前的软件交易信息
        System.out.println(linkDataBase.getSoftMessage());
        return ResponseResult.success().data("softinfo",linkDataBase.getSoftMessage());
    }

    /**
     * 第一步，添加被测软件的相关信息资料
     * @param softSimpleInfoVo 前端填写的软件信息
     * @return 被测对象的id主键
     */
    @PostMapping("/add")
    public ResponseResult addSoft(@RequestBody SoftSimpleInfoVo softSimpleInfoVo){
        //TODO 添加软件的信息到SoftInfo对象中，并返回该对象的主键ID给前端
        softInfo.setSoftName(softSimpleInfoVo.getSoftName());
        softInfo.setSoftDesc(softSimpleInfoVo.getSoftDesc());
        softInfo.setUserName(softSimpleInfoVo.getUserName());
        softInfo.setPhoneNum(softSimpleInfoVo.getPhoneNum());
        int id=0;
        try {
            id=linkDataBase.insertSoft(softSimpleInfoVo);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ResponseResult.success().data("id",id);
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
        if (file.isEmpty()) {
            return ResponseResult.error().message("上传失败，请选择文件");
        }
        if (file.getSize() < 1024) {
            return ResponseResult.error().message("上传失败，请选择大于1KB文件");
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
        softFileStore.setSoftInfoId(Integer.parseInt(softId));
        softFileStore.setSoftPath(softFile.getPath());
        System.out.println(softFileStore);
        //TODO 另外，将SoftFileStore对象返回的主键id保存至对应的软件信息对象SoftInfo的softFileStoreId属性中
        try {
            softInfo.setId(linkDataBase.insertSoftSotre(softFileStore));
            System.out.println(softInfo.getId());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ResponseResult.success();
    }

    /**
     * 第三步，审核当前被测软件，将当前软件的状态修改为待审核状态，等待审核即可
     * @param id 软件信息主键id
     * @return 无
     */
    @PostMapping("/verity/{id}")
    public ResponseResult softVerify(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        //TODO 修改SoftInfo对象的状态为：1软件待审核
        linkDataBase.updateStatus(Integer.parseInt(id));
        return ResponseResult.success();
    }
}
