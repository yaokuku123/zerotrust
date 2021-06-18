package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.service.ChainService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.web.bind.annotation.RequestMapping;

public class ChainController {

    ChainService chainService;

    @RequestMapping("/GET")
    public String getChainInfo() throws ShellChainException {
        String res = chainService.getChainInfo();
        return res;
    }
}
