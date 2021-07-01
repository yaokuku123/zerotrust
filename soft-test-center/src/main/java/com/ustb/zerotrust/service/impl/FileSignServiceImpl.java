package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.BlindVerify.Sign;
import com.ustb.zerotrust.domain.PublicKey;
import com.ustb.zerotrust.service.FileSignService;
import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.utils.FileUtil;
import com.ustb.zerotrust.utils.SerializeUtil;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

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

    @Autowired
    private ChainService chainService;

    @Autowired
    private LinkDataBase linkDataBase;


    /**
     * 对被测软件进行签名
     * @param fileName 软件名称
     * @param filePath 被测软件存储路径
     * @return 是否签名成功
     */
    @Override
    public File signFile(String fileName,String filePath) throws IOException, ShellChainException, SQLException, ClassNotFoundException {
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
            uLists.add(pairing.getG1().newRandomElement().getImmutable().getElementPowPreProcessing());
        }

        //签名阶段
        ArrayList<Element> signLists;
        Sign sign = new Sign();
        FileUtil fileUtil = new FileUtil();
        signLists = sign.sign(fileUtil, filePath, uLists, g, x, blockFileSize, pieceFileSize);

        //签名存储
        List<String> signStringList = new ArrayList<>();
        Base64.Encoder encoder = Base64.getEncoder();
        for (Element elm : signLists) {
            byte[] signByte = encoder.encode(elm.toBytes());
            signStringList.add(new String(signByte, "UTF-8"));
        }
        File signFile = fileStoreService.uploadFileSign(file.getName(), signStringList);


        //生成公钥对象
        PublicKey publicKey = new PublicKey(pairing,g, v, uLists);
        // 参数上客体链
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("g", publicKey.encodeG());
        attributes.put("v", publicKey.encodeV());
        attributes.put("uString", publicKey.encodeULists());
        attributes.put("pairParam",new String(Base64.getEncoder().encode(SerializeUtil.serialize(typeAParams).getBytes("UTF-8")),"UTF-8"));
        attributes.put("appName", fileName);
        attributes.put("fileSize",originFileSize);
        attributes.put("createTime",file.lastModified());
        attributes.put("fileType",file.getName().substring(file.getName().lastIndexOf(".")));
//        String toAddress = "1UAarmYDCCD1UQ6gtuyrWEyi25FoNQMvM8ojYe"; //主体链地址
        String toAddress = "1Wkg9jF48VeM16rUE9MSTu4dfyvJv4dAb5X1v";
        String txid = chainService.send2Obj(toAddress, 0, attributes);
        System.out.println(txid);
        //插入数据库
        linkDataBase.insertData(fileName, txid);

        return signFile;
    }
}
