package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.domain.DaemonSoft;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright(C),2019-2021,XXX公司
 * FileName: FilePathStoreMapper
 * Author: yaoqijun
 * Date: 2021/6/25 18:07
 */
public interface FilePathStoreMapper {

    /**
     * 插入被测软件的路径信息
     * @param daemonSoft 被测软件的相关信息
     */
    public void insertFilePath(DaemonSoft daemonSoft);

    /**
     * 根据软件名称获取软件的存储路径
     * @param fileName
     * @return
     */
    public DaemonSoft getFilePath(@Param("fileName") String fileName);
}
