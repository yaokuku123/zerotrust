package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Verify;
import com.ustb.zerotrust.domain.PublicKey;
import com.ustb.zerotrust.domain.QueryParam;
import com.ustb.zerotrust.domain.vo.QueryParamString;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.service.ChainClient;
import com.ustb.zerotrust.service.DaemonClient;

import com.ustb.zerotrust.service.impl.ChainService;
import com.ustb.zerotrust.util.ConvertUtil;
import com.ustb.zerotrust.utils.SerializeUtil;
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
    private ChainService chainService;

    @Autowired
    private LinkDataBase linkDataBase;


    /**
     * 查询验证是否通过
     * @param
     * @return 是否通过
     */
    @GetMapping("/result")
    public boolean checkResult(String fileName) throws Exception {

        //从链上获取参数
        String txid = linkDataBase.getTxid(fileName);
        String res = chainService.getFromObj(txid);
        JSONObject jsonObject = JSONObject.parseObject(res);
        //密码初始化部分
        Pairing pairing = null;
        try {
            pairing = PairingFactory.getPairing((PairingParameters) SerializeUtil.serializeToObject(new String(Base64.getDecoder().decode(jsonObject.get("pairParam").toString().getBytes("UTF-8")),"UTF-8")));
        }catch (Exception e){
            e.printStackTrace();
        }
        PublicKey publicKey = new PublicKey(pairing);
        Element g = publicKey.decodeG(jsonObject.get("g").toString());
        Element v = publicKey.decodeV(jsonObject.get("v").toString());
        ArrayList<ElementPowPreProcessing> uLists = publicKey.decodeULists(JSONArray.parseArray(jsonObject.get("uString").toString(), String.class));
        log.info("uLists:"+uLists);

        //查询 返回参数
        QueryParamString queryParamString = daemonClient.getMessage(fileName);
        String sigmaValues = queryParamString.getSigmasValues();
        ArrayList<String> viStringList = queryParamString.getViLists();
        ArrayList<String> miuStringList = queryParamString.getMiuLists();
        ArrayList<String> signStringList = queryParamString.getSignLists();
        log.info("sigmaValues :"+sigmaValues);
        log.info("viStringList :"+viStringList);
        log.info("miuStringList :"+miuStringList);
        log.info("signStringList :"+signStringList);

        //解析参数
        QueryParam queryParam = new QueryParam(pairing);
        ArrayList<Element> viLists = queryParam.decodeViLists(viStringList);
        ArrayList<Element> miuLists = queryParam.decodeMiuLists(miuStringList);
        ArrayList<Element> signLists = queryParam.decodeSignLists(signStringList);
        Element sigmasValues = queryParam.decodeSigma(sigmaValues);

//        开始验证
        Verify verify = new Verify();
        boolean result = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
        System.out.println(result);


        return result;
    }

}
