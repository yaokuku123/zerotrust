package com.ustb.zerotrust;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustb.zerotrust.service.impl.ChainService;
import com.ustb.zerotrust.utils.ConvertUtil;
import com.ustb.zerotrust.mapper.LinkDataBase;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class ConvertTest {

    @Autowired
    public ChainService chainService = new ChainService();

    private LinkDataBase linkDataBase = new LinkDataBase();
    private ConvertUtil convertUtil = new ConvertUtil();
    Base64.Encoder encoder = Base64.getEncoder();
    Base64.Decoder decoder = Base64.getDecoder();

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
            String uString = new String(encoder.encode(u.toBytes()), "UTF-8");
            uStringList.add(uString);
            uLists.add(u);
        }

        // JSONObject jsonObject = new JSONObject(attributes);
        // System.out.println(jsonObject.toString());//输出出错

        // 转化参数

        byte[] gByte1 = encoder.encode(g.toBytes());
        byte[] vByte1 = encoder.encode(v.toBytes());
        String gString = new String(gByte1, "UTF-8");
        String vString = new String(vByte1, "UTF-8");
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("g", gString);
        attributes.put("v", vString);
        attributes.put("uStringList", uStringList);

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
        List<String> uString = JSONArray.parseArray(jsonObject.get("uStringList").toString(), String.class);

        ArrayList<ElementPowPreProcessing> uNewList = new ArrayList<>();

        byte[] gDeByte = decoder.decode(gEnByte);
        byte[] vDeByte = decoder.decode(vEnByte);
        Element g1 = pairing.getG1().newElementFromBytes(gDeByte);
        Element v1 = pairing.getG1().newElementFromBytes(vDeByte);
        for(int i = 0; i< uStringList.size(); i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getField().getElementPowPreProcessingFromBytes(uString.get(i).getBytes());
            uNewList.add(u);
        }

        // 验证
        /*Verify verify = new Verify();
        boolean result = verify.verifyResult(pairing, g, uLists, v, sigmasValues, viLists, signLists, miuLists);
        System.out.println(result);*/

    }

    @Test
    public void Write2JsonFile() throws UnsupportedEncodingException, FileNotFoundException {
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
        HashMap<String, Object> uMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            ElementPowPreProcessing u = pairing.getG1().newRandomElement().getImmutable().getElementPowPreProcessing();
            String uString = new String(encoder.encode(u.toBytes()), "UTF-8");
            uStringList.add(uString);
            uLists.add(u);
        }

        uMap.put("uStringList", uStringList);
        String fileName = "exampleWrite.json";
        convertUtil.write2JsonFile(uMap, fileName);
//        HashMap<String, Object> hashMap = convertUtil.readfromJsonFile(fileName);

        /*// 标记文件生成是否成功
        boolean flag = true;
        String filePath = "C:\\Users\\KangXi\\zerotrust\\";
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath + "exampleWrite.json"),"UTF-8");
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(filePath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }

            file.createNewFile();
            // 将格式化后的字符串写入文件
            osw.write(JSON.toJSON(uMap).toString());
            osw.flush();
            osw.close();


        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        String jsonStr = "";
        try {
            File jsonFile = new File(filePath + "exampleWrite.json");
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();

        }

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);*/


    }

    public void testEnsure() {

    }

    @Test
    public void readJsonFile() {
        String myJsonObj2 = "{\n" +
                "    \"name\":\"网站\",\n" +
                "    \"num\":3,\n" +
                "    \"sites\": [\n" +
                "        { \"name\":\"Google\", \"info\":[ \"Android\", \"Google 搜索\", \"Google 翻译\" ] },\n" +
                "        { \"name\":\"Runoob\", \"info\":[ \"菜鸟教程\", \"菜鸟工具\", \"菜鸟微信\" ] },\n" +
                "        { \"name\":\"Taobao\", \"info\":[ \"淘宝\", \"网购\" ] }\n" +
                "    ]\n" +
                "}";
        JSONObject jsonobj2 = JSON.parseObject(myJsonObj2);

        JSONArray jsonArray = jsonobj2.getJSONArray("sites");
    }

}
