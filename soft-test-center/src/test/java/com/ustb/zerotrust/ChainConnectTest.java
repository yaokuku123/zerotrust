package com.ustb.zerotrust;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ustb.zerotrust.mapper.ChainDAO;
import com.ustb.zerotrust.service.impl.ChainService;
import com.ustb.zerotrust.mapper.LinkDataBase;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChainConnectTest {

    @Autowired
    ChainService chainService = new ChainService();

    ChainDAO chainDAO = new ChainDAO();
    LinkDataBase linkDataBase = new LinkDataBase();

    private String address = "";
    private String txid = "";

    @Test
    public void testGetInfo() throws ShellChainException {
        String res = chainService.getChainInfo();
        System.out.println(res);
    }

    @Test
    public void testSendCustom() throws ShellChainException, SQLException, ClassNotFoundException {
        JsonObject certificate = new JsonObject();
        certificate.addProperty("AppName", "calculator");
        certificate.addProperty("Address","adadad");

        address = "1Wkg9jF48VeM16rUE9MSTu4dfyvJv4dAb5X1v";
        HashMap<String,Object> appInfo = new HashMap<String, Object>();
        appInfo.put("appName", "calculator");
        appInfo.put("filePath", "/usr/local/calculator");
        appInfo.put("signPath", "/usr/local/calculator.sign");

        float amount = (float) 0.0;

        String res = chainService.send2Obj(address, amount, appInfo);
        System.out.println(res);
    }

    @Test
    public void testGetCertificate() throws ShellChainException {
        txid = "c10f12ae8fc86fd2869f6616c7e77ddb06de0fe33579dd0870d10776388cf442";
        String res = chainService.getFromObj(txid);
        JSONObject jsonObject = JSONObject.parseObject(res);
        System.out.println(jsonObject.get("appName"));
    }

    @Test
    public void connectMySQL() throws SQLException, ClassNotFoundException {
        //linkDataBase.getTxid("test1");
        linkDataBase.getTxid("testfile02.exe");
    }

    @Test
    public void json2HashMap() {
        String params="{\"version\":\"1.0\",\"charset\":\"UTF-8\",\"sign\":\"f94b7eb96d529bfe5b28d57288dd5b4ffb576c2ff98b2fa4ff2be6f70ab8b6e7\",\"signType\":\"SHA-256\",\"reqData\":{\"nextKeyValue\":\"\",\"dateTime\":\"20160825113210\",\"operatorNo\":\"9999\",\"merchantNo\":\"000054\",\"date\":\"20160116\",\"branchNo\":\"0755\"}}";

        HashMap<String, Object> hashMap=JSON.parseObject(params, HashMap.class);
        System.err.println("hashmap="+hashMap);

        Map<String, Object> map= JSON.parseObject(params, Map.class);
        System.out.println("map="+map);
    }
}
