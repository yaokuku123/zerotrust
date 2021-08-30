package com.ustb.zerotrust.service.Impl;

import com.ustb.zerotrust.mapper.ExtractDataDAO;
import com.ustb.zerotrust.mapper.ExtractTxidDAO;
import com.ustb.zerotrust.service.ExtractTxidService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author WYP
 * @date 2021-08-16 0:13
 */
@Service
public class ExtractTxidServiceImpl implements ExtractTxidService {

    @Resource
    private ExtractTxidDAO extractTxidDAO;

//    @Override
//    public void insert(String txid) {
//        extractTxidDAO.insert(txid);
//    }


    @Override
    public void insert(String txid, String fileName) {
        extractTxidDAO.insert(txid,fileName);
    }

    @Override
    public void insertRes(String txid, String fileName) {
        extractTxidDAO.insertRes(txid,fileName);
    }
}
