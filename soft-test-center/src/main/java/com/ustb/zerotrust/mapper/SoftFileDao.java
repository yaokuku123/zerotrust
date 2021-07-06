package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.entity.SoftFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author WYP
 * @date 2021-07-06 14:42
 */
@Mapper
public interface SoftFileDao {

    List<SoftFile> findAll();
}
