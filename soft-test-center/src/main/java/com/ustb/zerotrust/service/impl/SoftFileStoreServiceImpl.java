package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.entity.SoftFileStore;
import com.ustb.zerotrust.mapper.SoftFileStoreDAO;
import com.ustb.zerotrust.service.SoftFileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoftFileStoreServiceImpl implements SoftFileStoreService {

    @Autowired
    private SoftFileStoreDAO softFileStoreDAO;

    @Override
    public int insertSoftFile(SoftFileStore softFileStore) {
        int flag = softFileStoreDAO.insertSoftFile(softFileStore);
        return flag;
    }
}
