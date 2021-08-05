package com.ustb.zerotrust.mapper;

import com.ustb.zerotrust.domain.ExtractData;

public interface ExtractDataMapper {

    /**
     * 根据id查询信息
     * @param id 主键id
     * @return 查询结果
     */
    public ExtractData selectDataById(int id);

    /**
     * 更新数据
     * @param extractData 更新后的字段信息
     * @return 是否更新成功
     */
    public Boolean updateDate(ExtractData extractData);

    /**
     * 根据id删除数据
     * @param id 主键id
     * @return 是否删除成功
     */
    public Boolean deleteData(int id);


}
