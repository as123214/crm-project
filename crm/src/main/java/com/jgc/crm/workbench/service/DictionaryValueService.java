package com.jgc.crm.workbench.service;

import com.jgc.crm.workbench.domain.DictionaryValue;

import java.util.List;

public interface DictionaryValueService {
    List<DictionaryValue> selectDictionaryValueById(String id);
}
