package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.mapper.SoftReviewDAO;
import com.ustb.zerotrust.service.SoftReviewService;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * @author WYP
 * @date 2021-07-07 9:24
 */
@Service
public class SoftReviewServiceImpl implements SoftReviewService {

    @Resource
    private SoftReviewDAO softReviewDAO;

    @Autowired
    private FileSignStoreService fileSignStoreService;


    @Override
    public int PendingNums() {
        return softReviewDAO.PendingNums();
    }

    @Override
    public int RejectNums() {
        return softReviewDAO.RejectNums();
    }

    @Override
    public int HandledNums() {
        return softReviewDAO.HandledNums();
    }

    @Override
    public List<SoftInfo> getAllPending() {
        return softReviewDAO.getAllPending();
    }

    @Override
    public List<SoftInfo> getAllReject() {
        return softReviewDAO.getAllReject();
    }

    @Override
    public List<SoftInfo> getAllHandled() {
        return softReviewDAO.getAllHandled();
    }

    @Override
    public void examineSoftSuccess(Integer id) throws ClassNotFoundException, ShellChainException, SQLException, IOException, ParseException {
        //通过id 获取 file_name 和soft_path
        String file_name = softReviewDAO.findNameById(id);
        String path = softReviewDAO.findPathById(id);
        String txid = fileSignStoreService.signFile(file_name, path);
        //将交易id写入 id对应的soft_info
        softReviewDAO.addTxid(txid,id);
        softReviewDAO.examineSoftSuccess(id);
    }

    @Override
    public void examineSoftFail(Integer id) {
        softReviewDAO.examineSoftFail(id);
    }

    @Override
    public String findNameById(Integer id) {
        return softReviewDAO.findNameById(id);
    }

    @Override
    public String findPathById(Integer id) {
        return softReviewDAO.findPathById(id);
    }

    @Override
    public void addTxid(String txid, Integer id) {
        softReviewDAO.addTxid(txid,id);
    }

    @Override
    public String findTxidById(Integer id) {
        return softReviewDAO.findTxidById(id);
    }

    @Override
    public SoftInfo findById(Integer id) {
        return softReviewDAO.findById(id);
    }

    @Override
    public SoftInfo findByName(String name) {
        return softReviewDAO.findByName(name);
    }
}
