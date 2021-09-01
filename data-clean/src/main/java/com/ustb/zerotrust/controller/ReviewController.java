package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.FieldInfoWithCleanMethod;
import com.ustb.zerotrust.entity.VerifyResult;
import com.ustb.zerotrust.service.ChainService;
import com.ustb.zerotrust.service.ExtractTxidService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ExtractTxidService extractTxidService;

    @Autowired
    private ChainService chainService;

    @Value("${chainobj.address}")
    private String  chainObjAddresses;

    /**
     * 根据软件名称获取到清洗规则中需要清洗的字段和清洗方法
     * @param softName 软件名称
     * @return view安全等级 + 属性字段的清洗方法
     * @throws ShellChainException
     */
    @GetMapping("/getCleanFields")
    public ResponseResult getCleanFields(String softName) throws ShellChainException {
        String viewTxid = extractTxidService.getViewTxid(softName);
        JSONObject jsonObject = JSONObject.parseObject(chainService.getFromObj(viewTxid));
        String tableName = jsonObject.get("tableName").toString();
        Integer userSafeLevel = Double.valueOf(String.valueOf(jsonObject.get("userSafeLevel").toString())).intValue();
        String fieldInfoWithCleanMethodListStr = jsonObject.get("fieldInfoWithCleanMethodList").toString();
        List<FieldInfoWithCleanMethod> fieldInfoWithCleanMethodList = JSONObject.parseArray(fieldInfoWithCleanMethodListStr, FieldInfoWithCleanMethod.class);
        return ResponseResult.success().data("userSafeLevel",userSafeLevel).data("tableName",tableName).data("fieldInfoWithCleanMethodList",fieldInfoWithCleanMethodList);
    }

    /**
     * 获取前端的认证结果，将认证结果作为凭证记录至客体链保存，并在软件信息表中记录凭证交易id
     * @param verifyResult 审核结果
     * @return
     * @throws ShellChainException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @PostMapping("/verifyResult")
    public ResponseResult verifyResult(@RequestBody VerifyResult verifyResult) throws ShellChainException, SQLException, ClassNotFoundException {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("result",verifyResult.getResult());
        attributes.put("tableName",verifyResult.getTableName());
        attributes.put("fieldInfoWithCleanMethodList",verifyResult.getFieldInfoWithCleanMethodList());
        attributes.put("createTime",System.currentTimeMillis());
        String reviewTxid = chainService.send2Obj(chainObjAddresses, 0, attributes);
        extractTxidService.insertReviewTxid(verifyResult.getSoftName(),reviewTxid);
        return ResponseResult.success();
    }

//    @GetMapping("/test")
//    public ResponseResult test() throws ShellChainException {
//        String reviewTxid = "3f50c9fbf800547ba5607546b67dee6c17594b564bafbbc2d9d8580b82144d8a";
//        JSONObject jsonObject = JSONObject.parseObject(chainService.getFromObj(reviewTxid));
//        String result = jsonObject.get("result").toString();
//        String createTime = jsonObject.get("createTime").toString();
//        String fieldInfoWithCleanMethodListStr = jsonObject.get("fieldInfoWithCleanMethodList").toString();
//        List<FieldInfoWithCleanMethod> fieldInfoWithCleanMethodList = JSONObject.parseArray(fieldInfoWithCleanMethodListStr, FieldInfoWithCleanMethod.class);
//        return ResponseResult.success().data("result",result).data("createTime",createTime).data("fieldInfoWithCleanMethodList",fieldInfoWithCleanMethodList);
//    }
}
