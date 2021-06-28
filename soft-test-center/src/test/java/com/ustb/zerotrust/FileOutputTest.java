package com.ustb.zerotrust;

import com.ustb.zerotrust.utils.ConvertUtil;

import java.io.File;
import java.util.*;

public class FileOutputTest {
    public static void main(String[] args) {
        uploadFileSign("test", Arrays.asList("12","23"));
    }

    public static File uploadFileSign(String softName, List<String> signList) {
        //拼接签名文件的名称
        String signFileName = softName + ".sign";
        //保存签名文件
        String destPath = System.getProperty("user.dir") + "/uploadFile/"+ signFileName;

        Map<String,Object> map = new HashMap<>();
        map.put("signStringList",signList);
        ConvertUtil.write2JsonFile(map,destPath);
        return new File(destPath);
    }
}
