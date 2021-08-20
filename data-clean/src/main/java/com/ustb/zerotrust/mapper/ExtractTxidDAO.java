package com.ustb.zerotrust.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author WYP
 * @date 2021-08-15 23:52
 */
@Mapper
public interface ExtractTxidDAO {

    // extract_id
    void insert(String txid,String fileName);
}
