package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.domain.ExtractData;

public interface TaijiDataMapper {

    /**
     * 更新原表的数据
     * @param extractData 更新字段信息
     * @return 是否更新成功
     */
    public Boolean updateTaijiData(ExtractData extractData);

    /**
     * 删除原表数据
     * @param id 原表的主键id
     * @return 是否删除成功
     */
    public Boolean deleteTaijiData(int id);
}
