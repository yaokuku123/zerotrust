package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Check;
import com.ustb.zerotrust.BlindVerify.Verify;
import com.ustb.zerotrust.entity.PublicKey;
import com.ustb.zerotrust.entity.QueryParam;
import com.ustb.zerotrust.service.ChainClient;
import com.ustb.zerotrust.service.DaemonClient;

import com.ustb.zerotrust.util.ConvertUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;


import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

/**
 * @author WYP
 * @date 2021-06-25 10:24
 */
@RestController
@RequestMapping("/check")
@Slf4j
public class CheckAuthController {

    @Autowired
    private DaemonClient daemonClient;

    @Autowired
    private ChainClient chainClient;


    /**
     * 查询验证是否通过
     * @param
     * @return 是否通过
     */
    @GetMapping("/result")
    public String checkResult() throws Exception {

        QueryParam queryParam = new QueryParam();
        Base64.Decoder decoder = Base64.getDecoder();

        //查询 返回参数
        String s = daemonClient.getMessage();
        ConvertUtil convertUtil = new ConvertUtil();
        String fileName = "checkMessage.json";
        HashMap<String, Object> hashMap = convertUtil.readfromJsonFile(fileName);
        //vLists sigma miuList signList
        String sigmaValues = (String)hashMap.get("sigmaValues");
        List<String> viStringList = (List) hashMap.get("viStringList");
        List<String> miuStringList = (List)hashMap.get("miuStringList");
        List<String> signStringList = (List)hashMap.get("signStringList");
        log.info("sigmaValues :"+sigmaValues);
        log.info("viStringList :"+viStringList);
        log.info("miuStringList :"+miuStringList);
        log.info("signStringList :"+signStringList);


        //密码初始化部分
        int rbits = 53;
        int qbits = 1024;
        TypeACurveGenerator pg = new TypeACurveGenerator(rbits, qbits);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);
        //初始化相关参数
        Element g = pairing.getG1().newRandomElement().getImmutable();     //生成生成元
        Element x = pairing.getZr().newRandomElement().getImmutable();
        Element v = g.powZn(x);
        //生成U
        ArrayList<ElementPowPreProcessing> uLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getImmutable().getElementPowPreProcessing();
            uLists.add(u);
        }

        //从链上获取参数
        PublicKey publicKey = new PublicKey(pairing, g, v, uLists);
        JSONObject jsonObject = chainClient.getParam("typora-scrolls-0.5");

        g = publicKey.decodeG(jsonObject.get("g").toString());
        v = publicKey.decodeV(jsonObject.get("v").toString());
        uLists = publicKey.decodeULists(JSONArray.parseArray(jsonObject.get("uString").toString(), String.class));

        log.info("g:"+g);
        log.info("v:"+v);
        log.info("uLists:"+uLists);

        ArrayList<Element> viLists = queryParam.decodeViLists(viStringList);
        log.info("viLists :"+viLists);

        ArrayList<Element> miuLists = queryParam.decodeMiuLists(miuStringList);
        log.info("miuStringLists :"+miuLists);

        ArrayList<Element> signLists = queryParam.decodeSignLists(signStringList);
        log.info("signLists :"+signLists);

        byte[] signByte = decoder.decode(sigmaValues.toString().getBytes("UTF-8"));
        Element sigmasValues = pairing.getG1().newElementFromBytes(signByte).getImmutable();
        log.info("sigmasValues :"+sigmasValues);





//        开始验证
//        Element v = g.powZn(x);
//        Verify verify = new Verify();
//        boolean res = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
//        System.out.println(res);
//
//        Verify verify = new Verify();
//        res = verify.verifyResult(pairing, g, uList, v, sigmavalues, viList, signList, miuList);


        return s;
    }

//    @RequestMapping("/test")
//    public String getParam(){
//        String s = chainClient.getParam("typora-scrolls-0.5");
//        System.out.println(s);
//
//        return s;
//    }


}
