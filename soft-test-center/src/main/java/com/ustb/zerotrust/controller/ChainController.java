package com.ustb.zerotrust.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.ustb.zerotrust.entity.Application;
import com.ustb.zerotrust.service.ChainService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;

@RestController
public class ChainController {

    ChainService chainService = new ChainService();

    @RequestMapping("/GET")
    public String getChainInfo() throws ShellChainException {
        String res = chainService.getChainInfo();
        return res;
    }

    @RequestMapping(value = "/store", method = RequestMethod.POST, consumes = {"application/*;charset=UTF-8"})
    public String storeAppInfo(@RequestParam("address") String address,@RequestBody JsonNode certificate) throws ShellChainException, SQLException, ClassNotFoundException {
        HashMap<String, Object> hashMap= JSON.parseObject(certificate.toString(), HashMap.class);
        Application app = new Application(hashMap.get("appName").toString(), hashMap.get("filePath").toString(), hashMap.get("signPath").toString());

        String resAddress = chainService.sendCertificate(address, 0, hashMap);

        return resAddress;
    }

    @RequestMapping(value = "/getAppInfo", method = RequestMethod.GET)
    public String getAppInfo(@RequestParam("txid") String txid) throws ShellChainException {
        String res = chainService.getCertificate(txid);
        JSONObject jsonObject = JSONObject.parseObject(res);

        Application app = null;
        app.setAppName(jsonObject.get("appName").toString());
        app.setFilePath(jsonObject.get("filePath").toString());
        app.setSignPath(jsonObject.get("signPath").toString());


        return app.toString();

    }
}
