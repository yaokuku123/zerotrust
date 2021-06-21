package com.ustb.zerotrust.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ustb.zerotrust.dao.ChainDAO;
import com.ustb.zerotrust.util.LinkDataBase;
import edu.ustb.shellchainapi.bean.ChainParam;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import edu.ustb.utils.PropertiesUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

public class ChainService {

    private LinkDataBase linkDataBase = new LinkDataBase();
    private ChainDAO objChainDAO;
    private ChainDAO subChainDAO;
    boolean isMock;
    private String res = "";

    @Autowired
    public ChainService() {
        try {
            ChainParam objChainParam = loadShellchainConfig("OBJ");
            ChainParam subChainParam = loadShellchainConfig("SUB");
            subChainDAO = new ChainDAO(subChainParam);
            objChainDAO = new ChainDAO(objChainParam);
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

    public String getChainInfo() throws ShellChainException {
        res = objChainDAO.getInfo();
        return res;
    }

    public String send2Obj(String toAddress, float amount, Map<String,Object> attributes) throws ShellChainException, SQLException, ClassNotFoundException {
        res = objChainDAO.sendCustom(toAddress, amount, attributes);
        linkDataBase.insertData(attributes.get("appName").toString(), res);
        return res;
    }

    public String getFromObj(String txid) throws ShellChainException{
        res = objChainDAO.getCertificate(txid);
        return res;
    }

    public boolean verify(JSONObject jsonObject) {
        boolean result = false;

        //密码协议部分准备
        int rbits = 53;
        int qbits = 1024;
        TypeACurveGenerator pg = new TypeACurveGenerator(rbits, qbits);
        PairingParameters typeAParams = pg.generate();
        Pairing pairing = PairingFactory.getPairing(typeAParams);

        byte[] gByte = jsonObject.get("g").toString().getBytes();
        byte[] vByte = jsonObject.get("v").toString().getBytes();
        Base64.Decoder decoder = Base64.getDecoder();
        Element g = pairing.getG1().newElementFromBytes(decoder.decode(gByte));
        Element v = pairing.getZr().newElementFromBytes(decoder.decode(vByte));





        return result;
    }

    public String send2Sub(String toAddress, float amount, Map<String,Object> attributes) throws ShellChainException, SQLException, ClassNotFoundException {
        res = subChainDAO.sendCustom(toAddress, amount, attributes);
        // linkDataBase.insertData(attributes.get("appName").toString(), res);
        return res;
    }

    public String getFromSub(String txid) throws ShellChainException{
        res = subChainDAO.getCertificate(txid);
        return res;
    }
}
