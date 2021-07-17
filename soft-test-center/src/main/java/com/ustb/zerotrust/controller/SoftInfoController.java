package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import com.ustb.zerotrust.entity.vo.TxInfoVo;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.service.SoftFileStoreService;
import com.ustb.zerotrust.service.SoftInfoService;
import com.ustb.zerotrust.service.SoftReviewService;
import com.ustb.zerotrust.service.impl.ChainService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ustb.zerotrust.entity.SoftFileStore;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/soft")
@CrossOrigin
public class SoftInfoController {
    private LinkDataBase linkDataBase = new LinkDataBase();

    @Autowired
    private FileStoreService fileStoreService; //上传文件服务

    @Autowired
    private SoftInfoService softInfoService;

    @Autowired
    private SoftReviewService softReviewService;

    @Autowired
    private ChainService chainService;

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
     * 根据id删除文件路径字段以及置空软件信息保存的文件路径id字段
     * @param id 软件信息主键id
     * @return 无
     */
    @PostMapping("/deleteFile/{id}")
    public ResponseResult deleteFile(@PathVariable("id") int id){
        SoftInfo softInfo = softInfoService.getSoft(id);
        softInfoService.deleteFile(softInfo);
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


    //根据id查询txid 上链查询结果返回
    @GetMapping("/getSoftInfo")
    public ResponseResult getSoftInfo(@RequestParam("id") int id) throws ShellChainException {
        JSONObject jsonObject = null;
        try {
            Integer softId = softFileStoreService.getSoftId(id);
            String txid = softReviewService.findTxidById(softId);
            String res = chainService.getFromObj(txid);
            jsonObject = JSONObject.parseObject(res);
            JSONArray vout = jsonObject.getJSONArray("vout");
            String toAaddresses = vout.getJSONObject(0)
                    .getJSONObject("scriptPubKey")
                    .getJSONArray("addresses").get(0).toString();
            String blockHash = (String) jsonObject.get("blockhash");
            String softSize = String.valueOf(jsonObject.get("softSize"));
            String softType = (String) jsonObject.get("softType");
            String softName = (String) jsonObject.get("softName");
            String softDesc = (String) jsonObject.get("softDesc");
            String userName = (String) jsonObject.get("userName");
            String phoneNum = (String) jsonObject.get("phoneNum");
            String time = (String) jsonObject.get("createTime");
            Long creTime = Long.valueOf(time);
            Date date = new Date(creTime);
            TxInfoVo txInfoVo = new TxInfoVo();
            txInfoVo.setTxId(txid).setToAddress(toAaddresses).setBlockHash(blockHash)
            .setSoftName(softName).setSoftDesc(softDesc).setSoftSize(softSize)
            .setSoftType(softType).setUserName(userName).setPhoneNum(phoneNum)
            .setCreateTime(date);
            //return ResponseResult.success().data("txInfoVo",txInfoVo);
            return ResponseResult.success().data("jsonObject",jsonObject);
        } catch (ShellChainException e) {
            e.printStackTrace();
        }
        return ResponseResult.error();
    }
}
