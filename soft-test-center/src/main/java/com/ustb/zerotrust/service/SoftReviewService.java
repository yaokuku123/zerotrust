package com.ustb.zerotrust.service;

import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;

import java.util.List;

/**
 * @author WYP
 * @date 2021-07-07 9:23
 */
public interface SoftReviewService {

    int PendingNums();

    int RejectNums();

    int HandledNums();

    List<SoftSimpleInfoVo> getAllPending();

    List<SoftSimpleInfoVo> getAllReject();

    List<SoftSimpleInfoVo> getAllHandled();

    void examineSoftSuccess(Integer id);

    void examineSoftFail(Integer id);
}
