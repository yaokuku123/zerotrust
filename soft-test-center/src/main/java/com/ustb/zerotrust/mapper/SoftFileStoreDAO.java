package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.entity.SoftFileStore;

public interface SoftFileStoreDAO {

    /**
     * 插入软件存储路径对象信息
     * @param softFileStore 软件存储路径对象
     * @return 是否成功
     */
    int insertSoftFile(SoftFileStore softFileStore);

}
