package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.entity.ExtractData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author WYP
 * @date 2021-08-15 21:05
 */
@Mapper
public interface ExtractDataDAO {

    //查询出所有原数据
    List<ExtractData> findAll();

    //抽取字段作为view视图
    void ExtractAsView(ExtractData extractData);

    //delete
    void delete();


    List findByFieldName(String fieldName, String tableName);

    void insertExtractDate(String fieldName, List<String> list);

    void insert(@Param("map") Map<String, List<String>> map);

    List<Integer> count(String tableName);

    void initialTable(List<Integer> idList);

    void updateField(String fieldName, Map<Integer, String> map);
}
