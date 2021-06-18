package com.ustb.zerotrust.dao;

import edu.ustb.shellchainapi.bean.ChainParam;
import edu.ustb.shellchainapi.shellchain.command.ShellChainCommand;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;

public class ChainDAO {
    ShellChainCommand shellChainCommand;
    String res = "";

    public ChainDAO() {
        try {
            String Hello = "";
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChainDAO(ChainParam chainParam) {
        this();
        shellChainCommand = new ShellChainCommand(chainParam);
    }

    public String getInfo() throws ShellChainException {
        res = (String) shellChainCommand.getIssueCommand().getInfo();
        return res;
    }

    public String sendCustom(String AppName) throws ShellChainException {
        res = (String) shellChainCommand.getIssueCommand().sendCustom(AppName);
        return res;
    }
}
