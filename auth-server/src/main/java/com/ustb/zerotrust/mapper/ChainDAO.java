package com.ustb.zerotrust.mapper;

import edu.ustb.shellchainapi.bean.ChainParam;
import edu.ustb.shellchainapi.shellchain.command.ShellChainCommand;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
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

    public String sendCustom(String toAddress, float amount, Map<String,Object> attributes) throws ShellChainException {
        res = (String) shellChainCommand.getIssueCommand().sendCustom(toAddress, amount, attributes);
        return res;
    }

    public String getRaw(String txid) throws ShellChainException{
        String  res = (String) shellChainCommand.getIssueCommand().getRawTransaction(txid);
        return res;
    }
}
