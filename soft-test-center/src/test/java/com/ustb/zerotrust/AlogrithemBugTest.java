package com.ustb.zerotrust;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Check;
import com.ustb.zerotrust.BlindVerify.Sign;
import com.ustb.zerotrust.BlindVerify.Verify;
import com.ustb.zerotrust.domain.PublicKey;
import com.ustb.zerotrust.domain.QueryParam;
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
import java.sql.SQLException;
import java.util.*;

public class AlogrithemBugTest {
    public static void main(String[] args) throws UnsupportedEncodingException, ShellChainException, SQLException, ClassNotFoundException {
        String filePath = "/Users/yorick/Desktop/testFile05.exe";
        String signPath = "/Users/yorick/Desktop/testFile04.exe.sign";
        String txid = "1855d9adab400efa2db26782dd3555e608bce02b8e264913c230c57f0f289ce7";

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

        ChainService chainService = new ChainService();
        PublicKey publicKey = new PublicKey(pairing);
        //获取区块链数据并解析
        String res = chainService.getFromObj(txid);
        JSONObject jsonObject = JSONObject.parseObject(res);
        Element g = publicKey.decodeG(jsonObject.get("g").toString());
        Element v = publicKey.decodeV(jsonObject.get("v").toString());
        ArrayList<ElementPowPreProcessing> uLists = publicKey.decodeULists(JSONArray.parseArray(jsonObject.get("uString").toString(), String.class));
        //******************************转化验证************************************//

        //******************************转化验证************************************//
        ArrayList<Element> signLists = new ArrayList<>();
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
        FileUtil fileUtil = new FileUtil();
        miuLists = check.getMiuList(fileUtil, filePath, viLists,  blockFileSize, pieceFileSize);

        //******************************转化验证************************************//
        QueryParam queryParam = new QueryParam(pairing,sigmasValues,viLists,signLists,miuLists);
        sigmasValues = queryParam.decodeSigma(queryParam.encodeSigma());
        viLists = queryParam.decodeViLists(queryParam.encodeViList());
        signLists = queryParam.decodeSignLists(queryParam.encodeSignLists());
        miuLists = queryParam.decodeMiuLists(queryParam.encodeMiuLists());
        //******************************转化验证************************************//

        //开始验证
        Verify verify = new Verify();
        boolean result = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
        System.out.println(result);
    }
}
