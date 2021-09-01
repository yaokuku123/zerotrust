package com.ustb.zerotrust.service;

import com.ustb.zerotrust.entity.ExtractData;

import java.util.List;

/**
 * @author WYP
 * @date 2021-08-15 21:14
 */
public interface ExtractDataService {

    List<ExtractData> findAll();

    void ExtractAsView(ExtractData extractData);

    void delete();


    List findByFieldName(String fieldName, String tableName);


    void insertExtractDate(String fieldName, List<String> list);
}
