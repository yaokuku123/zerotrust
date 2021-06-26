package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Verify;
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

        //查询 返回参数
        String s = daemonClient.getMessage();
        ConvertUtil convertUtil = new ConvertUtil();
        String fileName = "checkMessage.json";
        HashMap<String, Object> hashMap = convertUtil.readfromJsonFile(fileName);
        //vLists sigma miuList signList
        String sigmaValues = (String)hashMap.get("sigmaValues");
        JSONArray viStringList = (JSONArray) hashMap.get("viStringList");
        JSONArray miuStringList = (JSONArray)hashMap.get("miuStringList");
        JSONArray signStringList = (JSONArray)hashMap.get("signStringList");
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
        //初始化相关参数 还原

//        Element g = pairing.getG1().newRandomElement().getImmutable();     //生成生成元
//        Element x = pairing.getZr().newRandomElement().getImmutable();


        Base64.Decoder decoder = Base64.getDecoder();
        ArrayList<Element> viLists = new ArrayList<>();
        ArrayList<Element> miuLists = new ArrayList<>();
        ArrayList<Element> signLists = new ArrayList<>();
        //List<> viStringLists = new ArrayList<>();

        for (Object elm : viStringList) {
            byte[] signByte = decoder.decode(elm.toString().getBytes("UTF-8"));
            viLists.add(pairing.getG1().newElementFromBytes(signByte).getImmutable());
        }
        log.info("viLists :"+viLists);

        for (Object elm : miuStringList) {
            byte[] signByte = decoder.decode(elm.toString().getBytes("UTF-8"));
            miuLists.add(pairing.getG1().newElementFromBytes(signByte).getImmutable());
        }
        log.info("miuStringLists :"+miuLists);

        for (Object elm : signStringList) {
            byte[] signByte = decoder.decode(elm.toString().getBytes("UTF-8"));
            signLists.add(pairing.getG1().newElementFromBytes(signByte).getImmutable());
        }
        log.info("signLists :"+signLists);

        byte[] signByte = decoder.decode(sigmaValues.toString().getBytes("UTF-8"));
        Element sigmasValues = pairing.getG1().newElementFromBytes(signByte).getImmutable();
        log.info("sigmasValues :"+sigmasValues);


        //从链上获取参数 还原
        JSONObject jsonObject = chainClient.getParam("typora-scrolls-0.5.zip");

        byte[] gEnByte = jsonObject.get("g").toString().getBytes();
        byte[] vEnByte = jsonObject.get("v").toString().getBytes();
        List<String> uStringList = JSONArray.parseArray(jsonObject.get("uStringList").toString(), String.class);

        ArrayList<ElementPowPreProcessing> uLists = new ArrayList<>();

        byte[] gDeByte = decoder.decode(gEnByte);
        byte[] vDeByte = decoder.decode(vEnByte);
        Element g = pairing.getG1().newElementFromBytes(gDeByte);
        Element x = pairing.getG1().newElementFromBytes(vDeByte);
        for(int i = 0; i< uStringList.size(); i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getField().getElementPowPreProcessingFromBytes(uStringList.get(i).getBytes());
            uLists.add(u);
        }




        //开始验证
//        Element v = g.powZn(x);
//        Verify verify = new Verify();
//        boolean res = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
//        System.out.println(res);

        return "ok";
    }




}
