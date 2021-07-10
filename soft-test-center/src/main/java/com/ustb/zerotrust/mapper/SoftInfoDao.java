package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.entity.SoftInfo;

import java.util.List;

public interface SoftInfoDao {

    /**
     * 获取软件的列表信息
     * @return 软件列表
     */
    List<SoftInfo> getSoftInfoList();

    /**
     * 插入数据
     * @param softInfo 软件信息
     * @return 是否成功
     */
    int insertSoftInfo(SoftInfo softInfo);

    /**
     * 获取对应id的软件信息
     * @param id 主键id
     * @return 对应的软件信息
     */
    SoftInfo getSoftInfo(int id);

    /**
     * 更新软件数据
     * @param softInfo 更新后的软件信息
     * @return 是否成功
     */
    int updateSoftInfo(SoftInfo softInfo);

    /**
     * 更新状态字段为待审核
     * @param softInfo 更新后的软件信息
     * @return 是否成功
     */
    int verifySoftInfo(SoftInfo softInfo);

    /**
     * 更新软件存储路径信息的id
     * @param softInfo 更新后的软件信息
     * @return 是否成功
     */
    int updateSoftFileId(SoftInfo softInfo);
}
