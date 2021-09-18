package com.ustb.zerotrust.service.impl;


import com.ustb.zerotrust.BlindVerify.Sign;
import com.ustb.zerotrust.domain.PublicKey;
import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.mapper.LinkDataBase;
import com.ustb.zerotrust.service.FileStoreService;
import com.ustb.zerotrust.service.FileTransFeignClient;
import com.ustb.zerotrust.service.SoftReviewService;
import com.ustb.zerotrust.utils.FileUtil;
import com.ustb.zerotrust.utils.SerializeUtil;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WYP
 * @date 2021-07-08 9:18
 */
@Service
public class FileSignStoreService {

    @Autowired
    private FileStoreService fileStoreService; //保存文件服务

    @Autowired
    private ChainService chainService;

    @Autowired
    private SoftReviewService softReviewService;

    @Autowired
    private FileTransFeignClient fileTransFeignClient; //远程发送文件服务



    public String signFile(String fileName, String filePath) throws IOException, ShellChainException, SQLException, ClassNotFoundException, ParseException {
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

        MultipartFile[] files = new MultipartFile[2];
        files[0] = new MockMultipartFile("files", file.getName(), ContentType.TEXT_PLAIN.toString(), new FileInputStream(file));
        files[1] = new MockMultipartFile("files", signFile.getName(), ContentType.TEXT_PLAIN.toString(), new FileInputStream(signFile));
        fileTransFeignClient.downLoad(fileName, files);

        //生成公钥对象
        PublicKey publicKey = new PublicKey(pairing,g, v, uLists);
        // 参数上客体链
        //通过 fileName 获取softInfo
        String res = "";
        SoftInfo softInfo = softReviewService.findByName(fileName);
        try {
            Date createTime = softInfo.getCreateTime();
            System.out.println(createTime);
            long time = createTime.getTime();
            res = String.valueOf(time);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("g", publicKey.encodeG());
        attributes.put("v", publicKey.encodeV());
        attributes.put("uString", publicKey.encodeULists());
        attributes.put("pairParam",new String(Base64.getEncoder().encode(SerializeUtil.serialize(typeAParams).getBytes("UTF-8")),"UTF-8"));
        attributes.put("softSize",originFileSize);
        attributes.put("softName", softInfo.getSoftName());
        attributes.put("softDesc",softInfo.getSoftDesc());
        attributes.put("userName",softInfo.getUserName());
        attributes.put("phoneNum",softInfo.getPhoneNum());
        attributes.put("createTime",res);
        attributes.put("softType",file.getName().substring(file.getName().lastIndexOf(".")));
//        String toAddress = "1UAarmYDCCD1UQ6gtuyrWEyi25FoNQMvM8ojYe"; //主体链地址
        String toAddress = "1MwnMpYGXy5TKJHMqiDy6MXorkQ5Nh6UoWCFEs";
        String txid = chainService.send2Obj(toAddress, 0, attributes);
        System.out.println(txid);

        return txid;
    }


}
