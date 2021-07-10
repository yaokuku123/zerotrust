package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import com.ustb.zerotrust.mapper.SoftInfoDao;
import com.ustb.zerotrust.service.SoftFileStoreService;
import com.ustb.zerotrust.service.SoftInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoftInfoServiceImpl implements SoftInfoService {

    @Autowired
    private SoftInfoDao softInfoDao;

    @Autowired
    private SoftFileStoreService softFileStoreService;

    @Override
    public List<SoftInfo> getSoftMessage() {
        return softInfoDao.getSoftInfoList();
    }

    @Override
    public int insertSoft(SoftInfo softInfo) {
        int flag = softInfoDao.insertSoftInfo(softInfo);
        return flag;
    }

    @Override
    public SoftInfo getSoft(int id) {
        SoftInfo softInfo = softInfoDao.getSoftInfo(id);
        return softInfo;
    }

    @Override
    public int updateSoft(SoftInfo softInfo) {
        int flag = softInfoDao.updateSoftInfo(softInfo);
        return flag;
    }

    @Override
    public int verifySoft(SoftInfo softInfo) {
        int flag = softInfoDao.verifySoftInfo(softInfo);
        return flag;
    }

    @Override
    public int updateSoftFileId(SoftInfo softInfo) {
        int flag = softInfoDao.updateSoftFileId(softInfo);
        return flag;
    }

    @Override
    public int deleteFile(SoftInfo softInfo) {
        softFileStoreService.deleteSoftFile(softInfo.getSoftFileStoreId());
        softInfo.setSoftFileStoreId(null);
        int flag = softInfoDao.updateSoftFileId(softInfo);
        return flag;
    }
}
