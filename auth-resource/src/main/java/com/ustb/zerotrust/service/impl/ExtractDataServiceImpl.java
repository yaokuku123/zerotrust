package com.ustb.zerotrust.service.impl;

import com.ustb.zerotrust.domain.ExtractData;
import com.ustb.zerotrust.mapper.ExtractDataMapper;
import com.ustb.zerotrust.mapper.TaijiDataMapper;
import com.ustb.zerotrust.service.ExtractDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtractDataServiceImpl implements ExtractDataService {

    @Autowired
    private ExtractDataMapper extractDataMapper;

    @Autowired
    private TaijiDataMapper taijiDataMapper;

    @Override
    public ExtractData selectDataById(int id) {
        return extractDataMapper.selectDataById(id);
    }

    @Override
    public Boolean updateData(ExtractData extractData) {
        return extractDataMapper.updateDate(extractData) && taijiDataMapper.updateTaijiData(extractData);
    }

    @Override
    public Boolean deleteData(int id) {
        return taijiDataMapper.deleteTaijiData(extractDataMapper.selectDataById(id).getTaijiCloudId())
                        && extractDataMapper.deleteData(id);
    }

    @Override
    public List<ExtractData> selectAll() {
        return extractDataMapper.selectAll();
    }
}
