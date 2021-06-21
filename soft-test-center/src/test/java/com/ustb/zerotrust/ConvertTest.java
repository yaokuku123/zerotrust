package com.ustb.zerotrust;

import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Verify;
import com.ustb.zerotrust.service.ChainService;
import com.ustb.zerotrust.util.LinkDataBase;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class ConvertTest {

    @Autowired
    public ChainService chainService = new ChainService();

    private LinkDataBase linkDataBase = new LinkDataBase();

    @Test
    public void convert() throws ShellChainException, SQLException, ClassNotFoundException, UnsupportedEncodingException {
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
        ArrayList<String> uStringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getImmutable().getElementPowPreProcessing();
            String uString = new String(u.toBytes(), "UTF-8");
            uStringList.add(uString);
            uLists.add(u);
        }

        // JSONObject jsonObject = new JSONObject(attributes);
        // System.out.println(jsonObject.toString());//输出出错

        // 转化参数
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] gByte1 = encoder.encode(g.toBytes());
        byte[] vByte1 = encoder.encode(v.toBytes());
        String gString = new String(gByte1, "UTF-8");
        String vString = new String(vByte1, "UTF-8");
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("g", gString);
        attributes.put("v", vString);
        // attributes.put("uStringList", uStringList);

        // 上链
        String toAddress = "1UAarmYDCCD1UQ6gtuyrWEyi25FoNQMvM8ojYe";
        String txid = chainService.send2Sub(toAddress, 0, attributes);

        // 从链上取回结果
        String res = chainService.getFromSub(txid);
        JSONObject jsonObject = JSONObject.parseObject(res);
        linkDataBase.insertData("calculator", txid);

        // 还原参数
        byte[] gEnByte = jsonObject.get("g").toString().getBytes();
        byte[] vEnByte = jsonObject.get("v").toString().getBytes();

        /*
        byte[] gEnByte = gString.getBytes();
        byte[] vEnByte = vString.getBytes();
        */

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] gDeByte = decoder.decode(gEnByte);
        byte[] vDeByte = decoder.decode(vEnByte);
        Element g1 = pairing.getG1().newElementFromBytes(gDeByte);
        Element v1 = pairing.getG1().newElementFromBytes(vDeByte);
        ArrayList<ElementPowPreProcessing> uNewStringList = new ArrayList<>();
        for(int i = 0; i< uStringList.size(); i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getField().getElementPowPreProcessingFromBytes(uStringList.get(i).getBytes());
            uNewStringList.add(u);
        }

        // 验证
        /*Verify verify = new Verify();
        boolean result = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
        System.out.println(result);*/

    }

    @Test
    public void convertBack() throws ShellChainException {
        String txid = "aeb9d42188586fa3428ddc45a7e12d6cf950e756489dc9d2395288b6e03258df";
        chainService.getFromSub(txid);


    }


}
