package com.ustb.zerotrust.service;

import edu.ustb.shellchainapi.shellchain.command.ShellChainException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FileSignService
 * Author: yaoqijun
 * Date: 2021/6/15 10:13
 */
public interface FileSignService {

    /**
     * 对被测软件进行签名
     * @param filePath 被测软件存储路径
     * @return 是否签名成功
     */
    public boolean signFile(String filePath) throws UnsupportedEncodingException, ShellChainException, SQLException, ClassNotFoundException, FileNotFoundException;
}
