package com.ustb.zerotrust.util;

import java.util.HashMap;
import java.util.Map;

public class ConvertUtil {

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
}
