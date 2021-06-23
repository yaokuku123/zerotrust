package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.ustb.zerotrust.entity.Application;
import com.ustb.zerotrust.service.ChainService;
import com.ustb.zerotrust.util.LinkDataBase;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RestController
public class ChainController {

    private ChainService chainService = new ChainService();
    private LinkDataBase linkDataBase = new LinkDataBase();
    private Base64.Decoder decoder = Base64.getDecoder();

    @RequestMapping("/GET")
    public String getChainInfo() throws ShellChainException {
        String res = chainService.getChainInfo();
        return res;
    }

    @RequestMapping(value = "/store", method = RequestMethod.POST, consumes = {"application/*;charset=UTF-8"})
    public String storeAppInfo(@RequestParam("address") String address,@RequestBody JsonNode certificate) throws ShellChainException, SQLException, ClassNotFoundException {
        HashMap<String, Object> hashMap= JSON.parseObject(certificate.toString(), HashMap.class);
        Application app = new Application(hashMap.get("appName").toString(), hashMap.get("filePath").toString(), hashMap.get("signPath").toString());

        String resAddress = chainService.send2Obj(address, 0, hashMap);

        return resAddress;
    }

    public boolean ensure(String appName) throws SQLException, ClassNotFoundException, ShellChainException {
        boolean res = false;
        String txid = linkDataBase.getTxid(appName);
        JSONObject jsonObject = JSONObject.parseObject(chainService.getFromObj(txid));

        //密码协议部分准备
        int rbits = 53;
        int qbits = 1024;
        TypeACurveGenerator pg = new TypeACurveGenerator(rbits, qbits);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);

        // 还原参数
        byte[] gEnByte = jsonObject.get("g").toString().getBytes();
        byte[] vEnByte = jsonObject.get("v").toString().getBytes();
        List<String> uStringList = JSONArray.parseArray(jsonObject.get("uStringList").toString(), String.class);

        ArrayList<ElementPowPreProcessing> uList = new ArrayList<>();

        byte[] gDeByte = decoder.decode(gEnByte);
        byte[] vDeByte = decoder.decode(vEnByte);
        Element g = pairing.getG1().newElementFromBytes(gDeByte);
        Element v = pairing.getG1().newElementFromBytes(vDeByte);
        for(int i = 0; i< uStringList.size(); i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getField().getElementPowPreProcessingFromBytes(uStringList.get(i).getBytes());
            uList.add(u);
        }

        return res;
    }

    /*@RequestMapping(value = "/getAppInfo", method = RequestMethod.GET)
    public String getAppInfo(@RequestParam("txid") String txid) throws ShellChainException {
        String res = chainService.getFromObj(txid);
        JSONObject jsonObject = JSONObject.parseObject(res);

        Application app = null;
        app.setAppName(jsonObject.get("appName").toString());
        app.setFilePath(jsonObject.get("filePath").toString());
        app.setSignPath(jsonObject.get("signPath").toString());

        return app.toString();

    }*/
}
