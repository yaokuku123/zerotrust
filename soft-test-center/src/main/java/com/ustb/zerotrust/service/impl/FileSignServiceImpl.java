package com.ustb.zerotrust.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Sign;
import com.ustb.zerotrust.service.ChainService;
import com.ustb.zerotrust.service.FileSignService;
import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.util.ConvertUtil;
import com.ustb.zerotrust.util.LinkDataBase;
import com.ustb.zerotrust.utils.FileUtil;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileSignServiceImpl
 * Author: yaoqijun
 * Date: 2021/6/15 10:16
 */
@Service
public class FileSignServiceImpl implements FileSignService {

    @Autowired
    private FileStoreService fileStoreService; //保存文件服务

    private ChainService chainService = new ChainService();
    private LinkDataBase linkDataBase = new LinkDataBase();
    private ConvertUtil convertUtil = new ConvertUtil();

    Base64.Encoder encoder = Base64.getEncoder();
    Base64.Decoder decoder = Base64.getDecoder();

    /**
     * 对被测软件进行签名
     *
     * @param filePath 被测软件存储路径
     * @return 是否签名成功
     */
    @Override
    public boolean signFile(String filePath) throws UnsupportedEncodingException, ShellChainException, SQLException, ClassNotFoundException, FileNotFoundException {
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
        ArrayList<String> uStringList = new ArrayList<>();
        HashMap<Integer, String> uMap = new HashMap<>();
        String uString = "";
        String fileName = "exampleWrite.json";
        boolean flag = false;
        for (int i = 0; i < 10; i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getImmutable().getElementPowPreProcessing();
            uString = new String(encoder.encode(u.toBytes()), "UTF-8");
            uMap.put(i, uString);
            uStringList.add(uString);
            uLists.add(u);
        }

        // 参数上链
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] gByte1 = encoder.encode(g.toBytes());
        byte[] vByte1 = encoder.encode(v.toBytes());
        String gString = new String(gByte1, "UTF-8");
        String vString = new String(vByte1, "UTF-8");
        System.out.println(gString);

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("g", gString);
        attributes.put("v", vString);
        attributes.put("uString", uStringList);



        String toAddress = "1UAarmYDCCD1UQ6gtuyrWEyi25FoNQMvM8ojYe";
        String txid = chainService.send2Sub(toAddress, 0, attributes);

        if (txid != "") {
            flag = convertUtil.write2JsonFile(uMap, fileName);
        }

        String res = chainService.getFromObj(txid);
        // JSONObject jsonObject = JSONObject.parseObject(res);
        linkDataBase.insertData("calculator", txid);

        //签名阶段
        ArrayList<Element> signLists;
        Sign sign = new Sign();
        FileUtil fileUtil = new FileUtil();
        signLists = sign.sign(fileUtil, filePath, uLists, g, x, blockFileSize, pieceFileSize);

        //签名存储
        fileStoreService.uploadFileSign(file.getName(), StringUtils.join(signLists,","));

        return true;
    }
}
