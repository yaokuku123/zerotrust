package com.ustb.zerotrust.service;

import com.ustb.zerotrust.entity.SoftFile;

import java.util.List;

/**
 * @author WYP
 * @date 2021-07-06 14:51
 */
public interface SoftFileService {
    List<SoftFile> findAll();
}
