package com.ustb.zerotrust;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Check;
import com.ustb.zerotrust.BlindVerify.Verify;
import com.ustb.zerotrust.domain.PublicKey;
import com.ustb.zerotrust.service.impl.ChainService;
import com.ustb.zerotrust.utils.ConvertUtil;
import com.ustb.zerotrust.utils.FileUtil;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: TransMulitpartTest
 * Author: yaoqijun
 * Date: 2021/6/25 20:13
 */
public class TransMultipartTest {
    public static void main(String[] args) throws ShellChainException, UnsupportedEncodingException {
        int rbits = 53;
        int qbits = 1024;
        TypeACurveGenerator pg = new TypeACurveGenerator(rbits, qbits);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);

        //获取区块链数据并解析
        PublicKey publicKey = new PublicKey(pairing);
        String txid = "fae051486f33077a7eb0258101dbe38ca2c2d70246f4e74a7c99efcc574e8d2d";
        ChainService chainService = new ChainService();
        String res = chainService.getFromObj(txid);
        JSONObject jsonObject = JSONObject.parseObject(res);
        Element g = publicKey.decodeG(jsonObject.get("g").toString());
        Element v = publicKey.decodeV(jsonObject.get("v").toString());
        ArrayList<ElementPowPreProcessing> uLists = publicKey.decodeULists(JSONArray.parseArray(jsonObject.get("uString").toString(), String.class));

        //从本地恢复签名
        String signPath = "D:\\study\\code\\test\\zerotrust-demo\\daemonFile\\multichain-2.0-latest.tar4233df3bdec9458092023b89cf8f0bd2.gz.sign";
        Base64.Decoder decoder = Base64.getDecoder();
        String jsonFile = ConvertUtil.readfromJsonFile(signPath);
        JSONObject jsonObjectLocal = JSONObject.parseObject(jsonFile);
        JSONArray jsonArray = jsonObjectLocal.getJSONArray("signStringList");
        ArrayList<Element> signLists = new ArrayList<>();
        for (Object elm : jsonArray) {
            byte[] signByte = decoder.decode(elm.toString().getBytes("UTF-8"));
            signLists.add(pairing.getG1().newElementFromBytes(signByte).getImmutable());
        }

        //查询阶段
        Check check = new Check();
        //求viLists
        ArrayList<Element> viLists;
        viLists = check.getViList(pairing, signLists);
        //求sigma
        Element sigmasValues = check.getSigh(pairing, signLists, viLists);
        //求miu
        ArrayList<Element> miuLists;
        FileUtil fileUtil = new FileUtil();
        String filePath = "D:\\study\\code\\test\\zerotrust-demo\\daemonFile\\multichain-2.0-latest.tar4233df3bdec9458092023b89cf8f0bd2.gz";
        File file = new File(filePath);
        long originFileSize = file.length();
        int blockFileSize = (int) (originFileSize / 100);// 切割后的文件块大小
        int pieceFileSize = blockFileSize / 10;// 切割后的文件片大小
        miuLists = check.getMiuList(fileUtil, filePath, viLists,  blockFileSize, pieceFileSize);

        //开始验证
        Verify verify = new Verify();
        boolean result = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
        System.out.println(result);
    }
}
