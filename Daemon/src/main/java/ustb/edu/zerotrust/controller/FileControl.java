package ustb.edu.zerotrust.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import ustb.edu.zerotrust.service.FileGetMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileControl {

    private FileGetMessage td;

    @RequestMapping("/GetCheckMessage")
    public String GetMessage() throws UnsupportedEncodingException, FileNotFoundException {
        FileGetMessage.startTimer();
        Map<String , Object> hashMap = new HashMap<>();
        String res = FileGetMessage.GetCheckMessage();


        return res;
    }

    @RequestMapping("/hello2")
    public String Hello2() {
        return FileGetMessage.GetMessage();
    }



}
