package com.jgc.crm.workbench.service.Impl;

import com.jgc.crm.workbench.domain.Clue;
import com.jgc.crm.workbench.mapper.ClueMapper;
import com.jgc.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("clueSercie")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Override
    public int saveClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByCondition(Map<String, Object> map) {
        return clueMapper.selectClueByCondition(map);
    }

    @Override
    public int queryTotaoRows() {
        return clueMapper.selectTotalRows();
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }
}
