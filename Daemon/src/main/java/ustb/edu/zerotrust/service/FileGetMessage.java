package ustb.edu.zerotrust.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.ustb.zerotrust.BlindVerify.Check;
import com.ustb.zerotrust.BlindVerify.Sign;
import ustb.edu.zerotrust.util.ConvertUtil;
import com.ustb.zerotrust.utils.FileUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

public class FileGetMessage {
    static String s;
    static String d;

    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException, FileNotFoundException {
//        startTimer();
        System.out.println(GetCheckMessage());
//        s = GetMessage();
//        d = GetCheck();
//        System.out.println("这是主进程的文件信息"+s);
//        System.out.println("这是主进程的查询信息"+d);
    }
    public static String GetMessage(){
        //String filePath = "E:\\python学习资料\\chap0 关于Python.pptx";
        String filePath = "D:\\Downloads\\Typora\\typora-scrolls-0.5.zip";
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
    public static String GetCheckMessage() throws UnsupportedEncodingException, FileNotFoundException {
        //String filePath = "D:\\Work\\nsAlbum\\UnityPlayer.dll";
        String filePath = "D:\\Downloads\\Typora\\typora-scrolls-0.5.zip";


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
        //查询阶段
        Check check = new Check();
        //求viLists
        ArrayList<Element> viLists;
        viLists = check.getViList(pairing, signLists);
        //求sigma
        Element sigmasValues = check.getSigh(pairing, signLists, viLists);
        //求miu
        ArrayList<Element> miuLists;
        miuLists = check.getMiuList(fileUtil, filePath, viLists, blockFileSize, pieceFileSize);
        d = miuLists.toString();

        //封装miuLists
        ArrayList<String> miuString = new ArrayList<>();
        for(int i = 0; i< miuLists.size(); i++) {
//            System.out.println(miuLists.get(i));
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] uByte1 = encoder.encode(miuLists.get(i).toBytes());
            String uString = new String(uByte1, "UTF-8");
            miuString.add(uString);
        }
        //还原miuLists
        ArrayList<Element> miuNewString = new ArrayList<>();
        Base64.Decoder decoder = Base64.getDecoder();
        for(int i = 0; i< miuString.size(); i++) {

            Element u = pairing.getZr().newElementFromBytes(decoder.decode(miuString.get(i).getBytes()));
            miuNewString.add(u);
        }

        //封装viList

        ArrayList<String> viString = new ArrayList<>();
        for(int i = 0; i< viLists.size(); i++) {
//            System.out.println(miuLists.get(i));
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] vByte1 = encoder.encode(viLists.get(i).toBytes());
            String vString = new String(vByte1, "UTF-8");
            viString.add(vString);
        }
        //还原viLists
        ArrayList<Element> viNewString = new ArrayList<>();
        for(int i = 0; i< miuString.size(); i++) {

            Element u = pairing.getZr().newElementFromBytes(decoder.decode(viString.get(i).getBytes()));
            viNewString.add(u);
        }

        ArrayList<String> signStringList = new ArrayList<>();
        for(int i = 0; i< signLists.size(); i++) {
//            System.out.println(miuLists.get(i));
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] signByte1 = encoder.encode(signLists.get(i).toBytes());
            String signString = new String(signByte1, "UTF-8");
            signStringList.add(signString);
        }

        //还原sigmavalues
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] sigByte1 = encoder.encode(sigmasValues.toBytes());
        String sigString = new String(sigByte1,"UTF-8");
        Element NewSigvalues = pairing.getG1().newElementFromBytes(decoder.decode(sigString.getBytes()));
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("sigmaValues", sigString);
        attributes.put("viStringList", viString);
        attributes.put("miuStringList",miuString);
        attributes.put("signStringList", signStringList);
        // System.out.println(attributes);

        ConvertUtil convertUtil = new ConvertUtil();
        convertUtil.write2JsonFile(attributes, "checkMessage.json");
        String res = JSON.toJSON(attributes).toString();
        //System.out.println(res);
        return res;
    }
//延时程序
    public static void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                GetMessage();
                try {
                    GetCheckMessage();
                } catch (UnsupportedEncodingException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000 * 5, 1000 * 10);
    }

}




