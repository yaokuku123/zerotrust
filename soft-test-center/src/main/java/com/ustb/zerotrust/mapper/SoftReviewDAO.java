package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.entity.SoftInfo;
import com.ustb.zerotrust.entity.vo.SoftSimpleInfoVo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    //查询已经办理软件个数
    int HandledNums();

    //获取待处理软件摘要信息
    List<SoftInfo> getAllPending();

    //获取被驳回软件摘要信息
    List<SoftInfo> getAllReject();

    //获取已经办理软件摘要信息
    List<SoftInfo> getAllHandled();

    //审核通过待处理软件
    void examineSoftSuccess(Integer id);

    //审核驳回待处理软件
    void examineSoftFail(Integer id);

    //根据id获取名字
    String findNameById(Integer id);

    //根据id获取path
    String findPathById(Integer id);

    //通过审核后写入txid
    void addTxid(String txid,Integer id);

    //根据id查询txid
    String findTxidById(Integer id);

    //根据id查询软件信息
    SoftInfo findById(Integer id);

    //
    SoftInfo findByName(String name);



}
