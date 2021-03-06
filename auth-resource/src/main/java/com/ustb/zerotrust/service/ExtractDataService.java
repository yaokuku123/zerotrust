package com.ustb.zerotrust.service;

import com.ustb.zerotrust.domain.ExtractData;

import java.util.List;

public interface ExtractDataService {

    /**
     * 根据id获取信息
     */
    public ExtractData selectDataById(int id);

    /**
     * 更新数据
     */
    public Boolean updateData(ExtractData extractData);

    /**
     * 删除数据
     */
    public Boolean deleteData(int id);

    /**
     * 获取全部数据
     * @return
     */
    public List<ExtractData> selectAll();

}
