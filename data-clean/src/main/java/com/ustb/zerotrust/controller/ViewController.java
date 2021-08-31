package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.FieldCleanRule;
import com.ustb.zerotrust.entity.FieldInfo;
import com.ustb.zerotrust.entity.FieldInfoWithCleanMethod;
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
@RequestMapping("/view")
public class ViewController {

    @Autowired
    private ExtractTxidService extractTxidService;

    @Autowired
    private ChainService chainService;

    @Value("${chainobj.address}")
    private String  chainObjAddresses;

    /**
     * 根据软件名称获取客体链中保存的源数据字段和其安全等级
     * @param softName 软件名称
     * @return 数据字段+安全等级
     * @throws ShellChainException
     */
    @GetMapping("/getResourceData")
    public ResponseResult getResourceData(String softName) throws ShellChainException {
        String resourceTxid = extractTxidService.getResourceTxid(softName);
        String resourceDataStr = chainService.getFromObj(resourceTxid);
        String fieldInfosStr = JSONObject.parseObject(resourceDataStr).get("fieldInfos").toString();
        List<FieldInfo> fieldInfos = JSONObject.parseArray(fieldInfosStr, FieldInfo.class);
        return ResponseResult.success().data("fieldInfos",fieldInfos);
    }

    /**
     * 接收前端用户传递的最终清洗方法并记录至客体链
     * @param fieldCleanRule 用户传递的携带清洗方法的属性字段信息
     * @return 是否记录成功
     */
    @PostMapping("/dataCleanMethod")
    public ResponseResult dataCleanMethod(@RequestBody FieldCleanRule fieldCleanRule) throws ShellChainException, SQLException, ClassNotFoundException {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("userSafeLevel",fieldCleanRule.getUserSafeLevel());
        attributes.put("fieldInfoWithCleanMethodList",fieldCleanRule.getFieldInfoWithCleanMethodList());
        String viewTxid = chainService.send2Obj(chainObjAddresses, 0, attributes);
        extractTxidService.insertViewTxid(fieldCleanRule.getSoftName(),viewTxid);
        return ResponseResult.success();
    }
}
