package com.ustb.zerotrust.service.Impl;

import com.ustb.zerotrust.entity.ExtractData;
import com.ustb.zerotrust.mapper.ExtractDataDAO;
import com.ustb.zerotrust.service.ExtractDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Override
    public List findByFieldName(String fieldName, String tableName) {
        return extractDataDAO.findByFieldName(fieldName,tableName);
    }

    @Override
    public void insertExtractDate(String fieldName, List<String> list) {
        extractDataDAO.insertExtractDate(fieldName,list);
    }

    @Override
    public void insert(Map<String, List<String>> map) {
        extractDataDAO.insert(map);
    }

    @Override
    public List<Integer> count(String tableName) {
        return extractDataDAO.count(tableName);
    }

    @Override
    public void initial(List<Integer> idList) {
        extractDataDAO.initialTable(idList);
    }

    @Override
    public void updateField(String fieldName, Map<Integer, String> map) {
        extractDataDAO.updateField(fieldName,map);
    }
}
