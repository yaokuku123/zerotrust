package com.ustb.zerotrust;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Check;
import com.ustb.zerotrust.BlindVerify.Sign;
import com.ustb.zerotrust.BlindVerify.Verify;
import com.ustb.zerotrust.entity.PublicKey;
import com.ustb.zerotrust.entity.QueryParam;
import com.ustb.zerotrust.service.impl.ChainService;
import com.ustb.zerotrust.util.ConvertUtil;
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
import java.sql.SQLException;
import java.util.*;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: CovertToStringVerifyTest
 * Author: yaoqijun
 * Date: 2021/6/24 16:05
 */
public class CovertToStringVerifyTest {
    public static void main(String[] args) throws UnsupportedEncodingException, ShellChainException, SQLException, ClassNotFoundException {
        //String filePath = "D:\\study\\code\\test\\zerotrust-demo\\uploadFile\\multichain-2.0-latest.tar.gz";
        String filePath = "D:\\Downloads\\Typora\\typora-scrolls-0.5.zip";

        //初始化配置 默认规定为 100块，每块有10片
        File file = new File(filePath);
        long originFileSize = file.length();
        int blockFileSize = (int) (originFileSize / 100);// 切割后的文件块大小
        int pieceFileSize = blockFileSize / 10;// 切割后的文件片大小

        //密码协议部分准备
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

        //******************************转化验证************************************//
        PublicKey publicKey = new PublicKey(pairing, g, v, uLists);
        //上传至区块链
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("g", publicKey.encodeG());
        attributes.put("v", publicKey.encodeV());
        attributes.put("uString", publicKey.encodeULists());
        attributes.put("appName", file.getName());
        attributes.put("fileSize",originFileSize);
        attributes.put("createTime",file.lastModified());
        attributes.put("fileType",file.getName().substring(file.getName().lastIndexOf(".")));
        String toAddress = "1Wkg9jF48VeM16rUE9MSTu4dfyvJv4dAb5X1v";
        ChainService chainService = new ChainService();
        String txid = chainService.send2Obj(toAddress, 0, attributes);
        System.out.println(txid);
        //获取区块链数据并解析
        String res = chainService.getFromObj(txid);
        JSONObject jsonObject = JSONObject.parseObject(res);
        g = publicKey.decodeG(jsonObject.get("g").toString());
        v = publicKey.decodeV(jsonObject.get("v").toString());
        uLists = publicKey.decodeULists(JSONArray.parseArray(jsonObject.get("uString").toString(), String.class));
        //******************************转化验证************************************//

        //签名阶段
        ArrayList<Element> signLists;
        Sign sign = new Sign();
        FileUtil fileUtil = new FileUtil();
        signLists = sign.sign(fileUtil, filePath, uLists, g, x, blockFileSize, pieceFileSize);

        //******************************转化验证************************************//
        //转为json字符串，存本地
        Base64.Encoder encoder = Base64.getEncoder();
        List<String> signStringList = new ArrayList<>();
        for (Element elm : signLists) {
            byte[] elmByte = encoder.encode(elm.toBytes());
            signStringList.add(new String(elmByte,"UTF-8"));
        }
        Map<String,Object> map = new HashMap<>();
        String signPath = "D:\\study\\code\\test\\zerotrust-demo\\uploadFile\\signList.sig";
        map.put("signStringList",signStringList);
        ConvertUtil.write2JsonFile(map,signPath);

        //从本地恢复
        Base64.Decoder decoder = Base64.getDecoder();
        String jsonFile = ConvertUtil.readfromJsonFile(signPath);
        JSONObject jsonObjectLocal = JSONObject.parseObject(jsonFile);
        JSONArray jsonArray = jsonObjectLocal.getJSONArray("signStringList");
        signLists = new ArrayList<>();
        for (Object elm : jsonArray) {
            byte[] signByte = decoder.decode(elm.toString().getBytes("UTF-8"));
            signLists.add(pairing.getG1().newElementFromBytes(signByte).getImmutable());
        }
        //******************************转化验证************************************//

        //查询阶段
        Check check = new Check();
        //求viLists
        ArrayList<Element> viLists;
        viLists = check.getViList(pairing, signLists);
        //求sigma
        Element sigmasValues = check.getSigh(pairing, signLists, viLists);
        //求miu
        ArrayList<Element> miuLists;
        miuLists = check.getMiuList(fileUtil, filePath, viLists,  blockFileSize, pieceFileSize);

        //******************************转化验证************************************//
        QueryParam queryParam = new QueryParam(pairing,sigmasValues,viLists,signLists,miuLists);
        sigmasValues = queryParam.decodeSigma(queryParam.encodeSigma());
        ArrayList<Element> viLists2 = queryParam.decodeViLists(queryParam.encodeViList());
        signLists = queryParam.decodeSignLists(queryParam.encodeSignLists());
        miuLists = queryParam.decodeMiuLists(queryParam.encodeMiuLists());
        //******************************转化验证************************************//

        //开始验证
        Verify verify = new Verify();
        boolean result = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
        System.out.println(result);
    }
}
