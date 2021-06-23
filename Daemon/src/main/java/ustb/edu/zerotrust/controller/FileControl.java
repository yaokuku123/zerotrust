package ustb.edu.zerotrust.controller;

import ustb.edu.zerotrust.service.FileGetMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
public class FileControl {

    private FileGetMessage td;

    @RequestMapping("/GetCheckMessage")
    public String GetMessage() throws UnsupportedEncodingException {
        FileGetMessage.startTimer();
        System.out.println(FileGetMessage.GetCheckMessage());
        String a ="缺少的步骤是map到json的转化";

        return a;
    }

    @RequestMapping("/hello2")
    public String Hello2() {
        return FileGetMessage.GetMessage();
    }



}
