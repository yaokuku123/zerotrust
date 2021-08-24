package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.ExtractData;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.service.ChainService;
import com.ustb.zerotrust.service.ExtractDataService;
import com.ustb.zerotrust.service.ExtractTxidService;
import com.ustb.zerotrust.util.MD5Utils;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * @author WYP
 * @date 2021-08-15 21:16
 */
@RestController
public class ExtractDataController {

    @Resource
    private ExtractDataService extractDataService;

    @Autowired
    private ChainService chainService;

    @Autowired
    private LinkDataBase linkDataBase;

    @Value("${chainobj.address}")
    private String  chainObjAddresses;

    @Resource
    private ExtractTxidService extractTxidService;

    public static final Logger logger = LoggerFactory.getLogger(ExtractDataController.class);


    @GetMapping("/ExtractData")
    public ResponseResult findExtractData(String fileName) throws ShellChainException, SQLException, ClassNotFoundException {
        //清除extract_data
        extractDataService.delete();

        List<ExtractData> extractDataList = extractDataService.findAll();
        for (ExtractData extractData:extractDataList){
            //system.out.println(MD5Utils.code(extractData.getCloudIdCrypto()));
            extractData.setCloudIdCrypto(MD5Utils.generate(extractData.getCloudIdCrypto()));
            extractData.setIpCrypto(MD5Utils.generate(extractData.getIpCrypto()));
            extractData.setTaijiCloudId(extractData.getId());
            extractDataService.ExtractAsView(extractData);
        }
        // 实时清理过程 多个数据库做清理    数据的混杂

        // record
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("ExtractData","Extract Complete");

        String txid = chainService.send2Obj(chainObjAddresses, 0, attributes);
        logger.info("txid为：{}",txid);
        extractTxidService.insert(txid,fileName);

        return ResponseResult.success().data("ExtractData",extractDataList);
    }
}
