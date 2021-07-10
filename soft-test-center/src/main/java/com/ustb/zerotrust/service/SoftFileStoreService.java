package com.ustb.zerotrust.service;

import com.ustb.zerotrust.entity.SoftFileStore;

public interface SoftFileStoreService {

    /**
     * 插入软件存储路径对象信息
     * @param softFileStore 软件存储路径对象
     * @return 是否成功
     */
    int insertSoftFile(SoftFileStore softFileStore);
}
