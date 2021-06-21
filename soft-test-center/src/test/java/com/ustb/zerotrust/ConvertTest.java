package com.ustb.zerotrust;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ustb.zerotrust.service.ChainService;
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
        for (int i = 0; i < 10; i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getImmutable().getElementPowPreProcessing();
            uLists.add(u);
        }

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("g", g);
        attributes.put("v", v);
        attributes.put("uList", uLists);

        JSONObject jsonObject = new JSONObject(attributes);
        // System.out.println(jsonObject.toString());//输出出错

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] gByte1 = encoder.encode(g.toBytes());
        byte[] vByte1 = encoder.encode(v.toBytes());
        String gString = new String(gByte1, "UTF-8");
        String vString = new String(vByte1, "UTF-8");
        System.out.println(gString);

        byte[] gByte = gString.getBytes();

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodeSig = decoder.decode(gByte);

        Element ele = pairing.getG1().newElementFromBytes(decodeSig);

        /*String toAddress = "1UAarmYDCCD1UQ6gtuyrWEyi25FoNQMvM8ojYe";
        String res = chainService.send2Sub(toAddress, 0, attributes);*/

    }

    @Test
    public void convertBack() throws ShellChainException {
        String txid = "aeb9d42188586fa3428ddc45a7e12d6cf950e756489dc9d2395288b6e03258df";
        chainService.getFromSub(txid);


    }


}
