package com.ustb.zerotrust.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.BlindVerify.Check;
import com.ustb.zerotrust.BlindVerify.Sign;
import com.ustb.zerotrust.domain.DaemonSoft;
import com.ustb.zerotrust.domain.QueryParam;
import com.ustb.zerotrust.domain.vo.QueryParamString;
import com.ustb.zerotrust.mapper.FilePathStoreMapper;
import com.ustb.zerotrust.service.FileGetMessage;
import com.ustb.zerotrust.utils.ConvertUtil;
import com.ustb.zerotrust.utils.FileUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileGetMessageImpl implements FileGetMessage {

    @Autowired
    private FilePathStoreMapper filePathStoreMapper;

    static String s;
    static String d;


    public static String GetMessage(){
        //String filePath = "E:\\python学习资料\\chap0 关于Python.pptx";
        String filePath = "D:\\Downloads\\Typora\\unins000.exe";
        File file1 = new File(filePath);
        //封装程序的一些信息
        ArrayList<String> Filists = new ArrayList<String>();
        Filists.add(file1.getName());
        Filists.add(String.valueOf(file1.length()));
        //格式化日期
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateTime = df.format(file1.lastModified());
        Filists.add(dateTime);
        s =Filists.toString();
        System.out.println("这是守护进程的文件信息"+s);
        return s;
    }



    //调用这个函数获得map结果的结果。
    public QueryParamString getCheckMessage(String fileName) {
        //密码协议部分准备
        int rbits = 53;
        int qbits = 1024;
        TypeACurveGenerator pg = new TypeACurveGenerator(rbits, qbits);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);

        //获取被测软件的相关文件存储位置
        DaemonSoft daemonSoft = filePathStoreMapper.getFilePath(fileName);
        String filePath = daemonSoft.getSoftPath();
        String signPath = daemonSoft.getSignPath();
        //初始化配置 默认规定为 100块，每块有10片
        File file = new File(filePath);
        long originFileSize = file.length();
        int blockFileSize = (int) (originFileSize / 100);// 切割后的文件块大小
        int pieceFileSize = blockFileSize / 10;// 切割后的文件片大小

        //从本地恢复
        Base64.Decoder decoder = Base64.getDecoder();
        String jsonFile = ConvertUtil.readfromJsonFile(signPath);
        JSONObject jsonObjectLocal = JSONObject.parseObject(jsonFile);
        JSONArray jsonArray = jsonObjectLocal.getJSONArray("signStringList");
        ArrayList<Element> signLists = new ArrayList<>();
        for (Object elm : jsonArray) {
            byte[] signByte = new byte[0];
            try {
                signByte = decoder.decode(elm.toString().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
        miuLists = check.getMiuList(fileUtil, filePath, viLists, blockFileSize, pieceFileSize);

        QueryParam queryParam = new QueryParam(pairing,sigmasValues,viLists,signLists,miuLists);
        QueryParamString queryParamString = null;
        try {
            queryParamString = new QueryParamString(queryParam.encodeSigma(), queryParam.encodeViList(),
                    queryParam.encodeSignLists(), queryParam.encodeMiuLists());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return queryParamString;
    }
//延时程序
//    public static void startTimer() {
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                GetMessage();
//                try {
//                    GetCheckMessage();
//                } catch (UnsupportedEncodingException | FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task, 1000 * 5, 1000 * 10);
//    }

}




