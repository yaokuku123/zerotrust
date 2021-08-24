package com.ustb.zerotrust.service.impl;

import com.alibaba.fastjson.JSONObject;
import edu.ustb.shellchainapi.bean.ChainParam;
import edu.ustb.shellchainapi.shellchain.command.ShellChainCommand;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import edu.ustb.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class ChainService {

    ShellChainCommand shellChainCommand;
    boolean isMock;

    @Autowired
    public ChainService() {
        try {
            ChainParam reqChainParam = loadShellchainConfig("REQ");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    ChainParam loadShellchainConfig(String chain) {
        Properties shellchainProperties = PropertiesUtil.getProperties("chain.properties");
        String enableMock = shellchainProperties.getProperty("ENABLE_MOCK");
        if (enableMock!=null || Boolean.valueOf(enableMock)==Boolean.FALSE) {
            isMock = false;
            String chainName = shellchainProperties.getProperty(chain + "_SERVER_CHAINNAME");
            String serverIP = shellchainProperties.getProperty(chain + "_SERVER_IP");
            String serverPort = shellchainProperties.getProperty(chain + "_SERVER_PORT");
            String loginUser = shellchainProperties.getProperty(chain + "_SERVER_LOGIN");
            String loginPass = shellchainProperties.getProperty(chain + "_SERVER_PWD");
            return new ChainParam(1, chainName, serverIP, serverPort, loginUser, loginPass);
        }
        isMock = true;
        return null;
    }

    public boolean verifyTxid(String ownerTxid, String objTxid) throws ShellChainException {
        Boolean flag = false;
        shellChainCommand = new ShellChainCommand(loadShellchainConfig("REQ"));
        String reqTxid = "";
        reqTxid = (String) shellChainCommand.getIssueCommand().accessRequest(ownerTxid,objTxid,"");
        String res = (String) shellChainCommand.getIssueCommand()
                .getRawTransaction(reqTxid);
        JSONObject jsonObject = JSONObject.parseObject(res);
        if( jsonObject.get("result").equals("PERMIT")) {
            flag = true;
        }

        return flag;

    }
}
