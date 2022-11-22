package com.jgc.crm.workbench.service;

import com.jgc.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveClue(Clue clue);
    List<Clue> queryClueByCondition(Map<String,Object> map);
    int queryTotaoRows();
    Clue queryClueById(String id);
}
