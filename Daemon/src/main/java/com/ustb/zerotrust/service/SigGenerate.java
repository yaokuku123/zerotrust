package com.ustb.zerotrust.service;

import com.ustb.zerotrust.BlindVerify.Sign;
import com.ustb.zerotrust.utils.FileUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import com.ustb.zerotrust.ParamStore;

import java.io.File;
import java.util.ArrayList;

public class SigGenerate {
    public static void main(String[] args){
        String filePath = "E:\\python学习资料\\chap0 关于Python.pptx";
        File file1 = new File(filePath);
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
        ParamStore ad =new ParamStore();
        ad.signlist = signLists;
        System.out.println(signLists);
        System.out.println(ad.signlist);

    }
}
