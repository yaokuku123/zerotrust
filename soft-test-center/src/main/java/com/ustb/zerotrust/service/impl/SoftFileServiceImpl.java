package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.entity.SoftFile;
import com.ustb.zerotrust.mapper.SoftFileDao;
import com.ustb.zerotrust.service.SoftFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WYP
 * @date 2021-07-06 14:52
 */
@Service
public class SoftFileServiceImpl implements SoftFileService {

    @Autowired
    private SoftFileDao softFileDao;

    @Override
    public List<SoftFile> findAll() {
        return softFileDao.findAll();
    }
}
