package com.ustb.zerotrust.service;

import com.ustb.zerotrust.dao.ChainDAO;
import edu.ustb.shellchainapi.bean.ChainParam;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import edu.ustb.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class ChainService {

    private ChainDAO ObjChainDAO;
    boolean isMock;
    private String res = "";

    @Autowired
    public ChainService() {
        try {
            ChainParam chainParam = loadShellchainConfig();
            ObjChainDAO = new ChainDAO(chainParam);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    ChainParam loadShellchainConfig() {
        Properties shellchainProperties = PropertiesUtil.getProperties("chain.properties");
        String enableMock = shellchainProperties.getProperty("ENABLE_MOCK");
        if (enableMock!=null || Boolean.valueOf(enableMock)==Boolean.FALSE) {
            isMock = false;
            String chainName = shellchainProperties.getProperty("OBJ_SERVER_CHAINNAME");
            String serverIP = shellchainProperties.getProperty("OBJ_SERVER_IP");
            String serverPort = shellchainProperties.getProperty("OBJ_SERVER_PORT");
            String loginUser = shellchainProperties.getProperty("OBJ_SERVER_LOGIN");
            String loginPass = shellchainProperties.getProperty("OBJ_SERVER_PWD");
            return new ChainParam(1, chainName, serverIP, serverPort, loginUser, loginPass);
        }
        isMock = true;
        return null;
    }

    public String getChainInfo() throws ShellChainException {
        res = ObjChainDAO.getInfo();
        return res;
    }

    public String sendChainCustom(String AppName) throws ShellChainException {
        res = ObjChainDAO.sendCustom(AppName);
        return res;
    }
}
