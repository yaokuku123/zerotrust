package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.domain.ResponseResult;
import com.ustb.zerotrust.entity.*;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.mapper.TableDao;
import com.ustb.zerotrust.service.ChainService;
import com.ustb.zerotrust.service.ExtractDataService;
import com.ustb.zerotrust.service.ExtractTxidService;
import com.ustb.zerotrust.service.TableService;
import com.ustb.zerotrust.util.MD5Utils;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author WYP
 * @date 2021-08-15 21:16
 */
@RestController
@CrossOrigin
public class ExtractDataController {

    @Resource
    private ExtractDataService extractDataService;

    @Resource
    private TableService tableService;

    @Resource
    private TableDao tableDao;

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

        return ResponseResult.success();
    }


    @GetMapping("/dataFiled")
    public ResponseResult dataFiledView(String tableName){

        List<String> columnList = tableService.listTableColumn(tableName);

//       List<Map> maps = tableDao.listTableColumn(tableName);
//        return ResponseResult.success().data("info",maps);

       return ResponseResult.success().data("info",columnList);
    }

    @PostMapping("/fieldRecord")
    public ResponseResult fieldRecord(@RequestBody List<FieldInfo> fieldInfos,String fileName,String tableName) throws ShellChainException, SQLException, ClassNotFoundException {


        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("fieldInfos",fieldInfos);
        attributes.put("tableName",tableName);
        String txid = chainService.send2Obj(chainObjAddresses, 0, attributes);
        logger.info("txid为：{}",txid);

        extractTxidService.insertRes(txid,fileName);

        return ResponseResult.success();
    }


    @PostMapping("/extractDataV2")
    public ResponseResult extractData(String fileName) throws ShellChainException, SQLException, ClassNotFoundException {

        String reviewTxid = extractTxidService.getReviewTxid(fileName);
        String verifyResult = chainService.getFromObj(reviewTxid);
        String result = JSONObject.parseObject(verifyResult).get("result").toString();
        if (result.equals("false")){
            return ResponseResult.error();
        }
        List stringList = new ArrayList();
        Map<Integer,String> map = new HashMap<>();

        String viewTxid = extractTxidService.getViewTxid(fileName);
        JSONObject jsonObject = JSONObject.parseObject(chainService.getFromObj(viewTxid));
        String tableName = jsonObject.get("tableName").toString();
        String fieldInfoWithCleanMethodListStr = jsonObject.get("fieldInfoWithCleanMethodList").toString();
        List<FieldInfoWithCleanMethod> fieldInfoWithCleanMethodList = JSONObject.parseArray(fieldInfoWithCleanMethodListStr, FieldInfoWithCleanMethod.class);

        //清除extract_data 初始化
        extractDataService.delete();
        List<Integer> idList = extractDataService.count(tableName);
        extractDataService.initial(idList);
        FieldRecord fieldRecord = new FieldRecord();
        HashMap<String, Object> attributes = new HashMap<>();
        for (FieldInfoWithCleanMethod f : fieldInfoWithCleanMethodList){
            if (f.getCleanMethod().equals("hashClean")){
                //获取字段所有值 清洗
                fieldRecord.setFieldName(f.getFieldName()).setCleanMethod(f.getCleanMethod()).setCleanTime(new Date());
                String fieldName = f.getFieldName();
                attributes.put(fieldName,fieldRecord);
                List<String> hashList = new ArrayList<>();
                stringList = extractDataService.findByFieldName(f.getFieldName(),tableName);

                for (int i = 0;i < stringList.size();i++){
                    String code = MD5Utils.code((String)stringList.get(i));
                    map.put(idList.get(i),code);
                    //hashList.add(code);
                }

                //update
                extractDataService.updateField(f.getFieldName(),map);
                hashList.clear();
                stringList.clear();

            }else if (f.getCleanMethod().equals("saltClean")){

                fieldRecord.setFieldName(f.getFieldName()).setCleanMethod(f.getCleanMethod()).setCleanTime(new Date());
                String fieldName = f.getFieldName();
                attributes.put(fieldName,fieldRecord);
                List<String> saltList = new ArrayList<>();
                stringList = extractDataService.findByFieldName(f.getFieldName(),tableName);

                for (int i = 0;i < stringList.size();i++){
                    String code = MD5Utils.generate((String)stringList.get(i));
                    map.put(idList.get(i),code);
                }
                //update
                extractDataService.updateField(f.getFieldName(),map);
                saltList.clear();
                stringList.clear();
            }
        }

        // record
        String txid = chainService.send2Obj(chainObjAddresses, 0, attributes);
        ExtractLog log = new ExtractLog();
        log.setFileName(fileName).setExtractTxid(txid).setCleanTime(new Date());
        extractTxidService.insert(txid,fileName);
        extractTxidService.insertLog(log);

        return ResponseResult.success();
    }

}
