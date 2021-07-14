package com.ustb.zerotrust.service;

import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author WYP
 * @date 2021-07-07 9:23
 */
public interface SoftReviewService {

    int PendingNums();

    int RejectNums();

    int HandledNums();

    List<SoftInfo> getAllPending();

    List<SoftInfo> getAllReject();

    List<SoftInfo> getAllHandled();

    void examineSoftSuccess(Integer id) throws ClassNotFoundException, ShellChainException, SQLException, IOException;

    void examineSoftFail(Integer id);

    String findNameById(Integer id);

    String findPathById(Integer id);

    void addTxid(String txid,Integer id);

    String findTxidById(Integer id);

    SoftInfo findById(Integer id);
}
