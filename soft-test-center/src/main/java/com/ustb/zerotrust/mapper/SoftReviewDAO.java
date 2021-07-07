package com.ustb.zerotrust.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author WYP
 * @date 2021-07-07 9:17
 */
@Mapper
public interface SoftReviewDAO {

    //查询待处理软件个数
    int PendingNums();

    //查询被驳回软件个数
    int RejectNums();


}
