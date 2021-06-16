package com.ustb.zerotrust;

import com.ustb.zerotrust.BlindVerify.Check;
import com.ustb.zerotrust.BlindVerify.Sign;
import com.ustb.zerotrust.BlindVerify.Verify;
import com.ustb.zerotrust.utils.FileUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: BlindVerifyTest
 * Author: yaoqijun
 * Date: 2021/6/15 15:43
 */
public class BlindVerifyTest {
    public static void main(String[] args) throws IOException {
        String filePath = "D:\\study\\code\\test\\zerotrust-demo\\uploadFile\\test.txt";

        //初始化配置 默认规定为 100块，每块有10片
        File file = new File(filePath);
        long originFileSize = file.length();
        int pieceFileSize = (int) (originFileSize / 1000);// 切割后的文件片大小
        int blockFileSize = pieceFileSize * 10;// 切割后的文件块大小

        //密码协议部分准备
        int rbits = 53;
        int qbits = 1024;
        TypeACurveGenerator pg = new TypeACurveGenerator(rbits, qbits);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);
        //初始化相关参数
        Element g = pairing.getG1().newRandomElement().getImmutable();     //生成生成元
        Element x = pairing.getZr().newRandomElement().getImmutable();
        //生成U
        ArrayList<ElementPowPreProcessing> uLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getImmutable().getElementPowPreProcessing();
            uLists.add(u);
        }
        //签名阶段
        ArrayList<Element> signLists;
        Sign sign = new Sign();
        FileUtil fileUtil = new FileUtil();
        signLists = sign.sign(fileUtil, filePath, uLists, g, x, blockFileSize, pieceFileSize);

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

        //开始验证
        Element v = g.powZn(x);
        Verify verify = new Verify();
        boolean result = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
        System.out.println(result);
    }
}
