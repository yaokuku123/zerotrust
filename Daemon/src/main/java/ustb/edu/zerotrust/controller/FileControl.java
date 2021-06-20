package ustb.edu.zerotrust.controller;

import ustb.edu.zerotrust.service.FileGetMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileControl {

    private FileGetMessage td;
    @RequestMapping("/hello")
    public String Hello() {
        FileGetMessage.startTimer();
        return FileGetMessage.GetCheck();
    }
    @RequestMapping("/hello2")
    public String Hello2() {
        return FileGetMessage.GetMessage();
    }
    @RequestMapping("/hello3")
    public String Hello3() {
        return FileGetMessage.GetCheck();
    }
    @RequestMapping("/hello4")
    public String Hello4() {
        return FileGetMessage.GetCheck();
    }


}
