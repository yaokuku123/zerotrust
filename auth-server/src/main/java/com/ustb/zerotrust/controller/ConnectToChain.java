package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.CleanInfoVo;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.service.impl.ChainService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class ConnectToChain {
    @Autowired
    private CleanInfoVo cleanInfoVo;
    @Autowired
    private ChainService chainService;
    @Autowired
    private LinkDataBase linkDataBase;
    @Value("${chainobj.address}")
    private String  chainObjAddresses;
    @GetMapping("/ExtractData")
    public ResponseResult TransferResult(String fileName) throws ShellChainException, ClassNotFoundException, SQLException {

        //从数据库获得txid和extractid
        String txid1 = linkDataBase.getTxid(fileName);
        String extracid = linkDataBase.getExtractId(fileName);
        cleanInfoVo.setTxid(txid1);
        cleanInfoVo.setExtracid(extracid);
        // record
        HashMap<String, Object> attributes = new HashMap<>();

        attributes.put("CleanInfo",cleanInfoVo);


        String txid = chainService.send2Obj(chainObjAddresses, 0, attributes);

        return ResponseResult.success();
    }

}
