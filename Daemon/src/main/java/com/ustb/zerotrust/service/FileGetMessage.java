package com.ustb.zerotrust.service;

import com.ustb.zerotrust.domain.vo.QueryParamString;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;

import java.sql.SQLException;

public interface FileGetMessage {

    /**
     * 守护进程获取被测软件的查询参数信息
     * @param fileName
     * @return
     */
    public QueryParamString getCheckMessage(String fileName) throws SQLException, ClassNotFoundException, ShellChainException;
}
