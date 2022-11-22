package com.jgc.crm.workbench.service.Impl;

import com.jgc.crm.workbench.domain.DictionaryValue;
import com.jgc.crm.workbench.mapper.DictionaryValueMapper;
import com.jgc.crm.workbench.service.DictionaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("dictionaryValue")
public class DictionaryValueServiceImpl implements DictionaryValueService {
    @Autowired
    private DictionaryValueMapper dictionaryValueMapper;
    @Override
    public List<DictionaryValue> selectDictionaryValueById(String id) {
        return dictionaryValueMapper.selectDictionaryValueById(id);
    }
}
