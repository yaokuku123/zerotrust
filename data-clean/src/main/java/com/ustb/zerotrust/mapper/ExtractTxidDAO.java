package com.ustb.zerotrust.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author WYP
 * @date 2021-08-15 23:52
 */
@Mapper
public interface ExtractTxidDAO {

    // extract_id
    void insert(String txid,String fileName);

    void insertRes(String txid,String fileName);

    /**
     * 根据软件名称获取到源数据字段客体链的txid
     * @param softName 软件名称
     * @return 交易id
     */
    String getResourceTxid(String softName);

    /**
     * 向软件信息表中记录viewTxid
     * @param softName 软件名称
     * @param viewTxid viewTxid交易
     */
    void insertViewTxid(@Param("softName") String softName, @Param("viewTxid") String viewTxid);
}
