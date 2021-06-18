package com.ustb.zerotrust;

import com.ustb.zerotrust.dao.ChainDAO;
import com.ustb.zerotrust.service.ChainService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChainConnectTest {

    @Autowired
    ChainService chainService = new ChainService();

    ChainDAO chainDAO = new ChainDAO();

    @Test
    public void testGetInfo() throws ShellChainException {
        String res = chainService.getChainInfo();
        System.out.println(res);
    }

    @Test
    public void testSendCustom() throws ShellChainException {
        String res = chainService.sendChainCustom("calculator");
        System.out.println(res);
    }
}
