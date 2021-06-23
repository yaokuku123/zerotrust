package ustb.edu.zerotrust.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ConvertUtil {

    private String filePath = "C:\\Users\\KangXi\\zerotrust\\";

    public static Map<String,Object> getStringToMap(String str){
        if (str.startsWith("{")) {
            str = str.substring(1, str.length());
        }
        if (str.endsWith("}")) {
            str = str.substring(0, str.length() - 1);
        }

        //根据逗号截取字符串数组
        String[] str1 = str.split(",");
        //创建Map对象
        Map<String,Object> map = new HashMap<>();
        //循环加入map集合
        for (int i = 0; i < str1.length; i++) {
            //根据":"截取字符串数组
            String[] str2 = str1[i].split("=");
            //str2[0]为KEY,str2[1]为值
            // str2.length-1 为下标最大值
            if(str2.length-1 == 0){
                map.put(str2[0].trim(),"");
            }else{
                map.put(str2[0].trim(),str2[1].trim());
            }

        }
        return map;
    }

    // write ulist to json
    public boolean write2JsonFile(HashMap<String, Object> eMap, String fileName) throws FileNotFoundException, UnsupportedEncodingException {

        // 标记文件生成是否成功
        boolean flag = true;

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath + fileName),"UTF-8");
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
            osw.write(JSON.toJSON(eMap).toString());
            osw.flush();
            osw.close();


        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        return flag;
    }

    public HashMap<String, Object> readfromJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(filePath + fileName);
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

        HashMap<String,Object> hashMap = new HashMap<>();
        String sigmaString = "";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);

        hashMap.put("sigmaValues", jsonObject.get("sigmaValues").toString());
        hashMap.put("viStringList", jsonObject.getJSONArray("viStringList"));
        hashMap.put("miuStringList", jsonObject.getJSONArray("miuStringList"));
        hashMap.put("signStringList", jsonObject.getJSONArray("signStringList"));

        return hashMap;
        // Iterator iter = jsonObject.entrySet().iterator();

        /*while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if(entry.getKey() == "sigmaValues")
            {

            }else if(entry.getKey() == "viStringList"){
                viStringList.add(entry.getValue().toString());
            }else if(entry.getKey() == "miuStringList"){
                miuStringList = jsonObject
            }

        }*/
    }

    // Base64 encode element to string
    public String element2String(Element e) throws UnsupportedEncodingException {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] eByte = encoder.encode(e.toBytes());
        String eString = new String(eByte, "UTF-8");

        return eString;
    }

    // Base64 decode String to element
/*    public Element string2Element(String s) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] sByte = s.getBytes();

    }*/
}
