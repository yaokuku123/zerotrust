package com.ustb.zerotrust.controller;

import com.ustb.zerotrust.domain.vo.QueryParamString;
import com.ustb.zerotrust.service.FileGetMessage;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;


@RestController
public class FileControlV2 {

    @Autowired
    private FileGetMessage fileGetMessage;

    @GetMapping("/GetCheckMessageV2")
    public QueryParamString GetMessage(String fileName) throws ShellChainException, SQLException, ClassNotFoundException {
        QueryParamString checkMessage = fileGetMessage.getCheckMessageV2(fileName);
        return checkMessage;
    }

}
