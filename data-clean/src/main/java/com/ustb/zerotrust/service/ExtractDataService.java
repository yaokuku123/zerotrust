package com.ustb.zerotrust.service;

import com.ustb.zerotrust.entity.ExtractData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    void insert(Map<String, List<String>> map);

    List<Integer> count(String tableName);

    void initial(List<Integer> idList);

    void updateField(String fieldName,Map<Integer, String> map);
}
