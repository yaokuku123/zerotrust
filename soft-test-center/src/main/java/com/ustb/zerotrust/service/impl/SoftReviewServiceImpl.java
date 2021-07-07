package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.mapper.SoftReviewDAO;
import com.ustb.zerotrust.service.SoftReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
