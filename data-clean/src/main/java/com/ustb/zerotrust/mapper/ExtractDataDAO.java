package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.entity.ExtractData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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



}
