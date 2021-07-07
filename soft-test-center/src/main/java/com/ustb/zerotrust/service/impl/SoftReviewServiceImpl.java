package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import com.ustb.zerotrust.mapper.SoftReviewDAO;
import com.ustb.zerotrust.service.SoftReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author WYP
 * @date 2021-07-07 9:24
 */
@Service
public class SoftReviewServiceImpl implements SoftReviewService {

    @Autowired
    private SoftReviewDAO softReviewDAO;


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
    public List<SoftSimpleInfoVo> getAllPending() {
        return softReviewDAO.getAllPending();
    }

    @Override
    public List<SoftSimpleInfoVo> getAllReject() {
        return softReviewDAO.getAllReject();
    }

    @Override
    public List<SoftSimpleInfoVo> getAllHandled() {
        return softReviewDAO.getAllHandled();
    }

    @Override
    public void examineSoftSuccess(Integer id) {
        softReviewDAO.examineSoftSuccess(id);
    }

    @Override
    public void examineSoftFail(Integer id) {
        softReviewDAO.examineSoftFail(id);
    }
}
