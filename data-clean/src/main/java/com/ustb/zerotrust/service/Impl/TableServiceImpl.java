package com.ustb.zerotrust.service.Impl;

import com.ustb.zerotrust.mapper.TableDao;
import com.ustb.zerotrust.service.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WYP
 * @date 2021-08-30 16:23
 */
@Service
public class TableServiceImpl implements TableService {

    @Resource
    private TableDao tableDao;

    public static final Logger log = LoggerFactory.getLogger(TableServiceImpl.class);

    @Override
    public List<String> listTableColumn(String tableName) {
        List<Map> maps = tableDao.listTableColumn(tableName);
        List<String> column = new ArrayList<>();
        for (Map map : maps ) {
            column.add((String) map.get("COLUMN_NAME"));
        }
        log.info("column {}",column);
        return column;
    }
}
