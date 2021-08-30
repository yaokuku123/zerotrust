package com.ustb.zerotrust.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author WYP
 * @date 2021-08-30 16:03
 */
@Mapper
public interface TableDao {

    List<Map> listTableColumn(String tableName);
}
