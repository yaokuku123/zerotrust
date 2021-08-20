package com.ustb.zerotrust.service.Impl;

import com.ustb.zerotrust.entity.ExtractData;
import com.ustb.zerotrust.mapper.ExtractDataDAO;
import com.ustb.zerotrust.service.ExtractDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author WYP
 * @date 2021-08-15 21:14
 */
@Service
public class ExtractDataServiceImpl implements ExtractDataService {

    @Resource
    private ExtractDataDAO extractDataDAO;

    @Override
    public List<ExtractData> findAll() {
        return extractDataDAO.findAll() ;
    }


    @Override
    public void ExtractAsView(ExtractData extractData) {
        extractDataDAO.ExtractAsView(extractData);
    }

    @Override
    public void delete() {
        extractDataDAO.delete();
    }
}
