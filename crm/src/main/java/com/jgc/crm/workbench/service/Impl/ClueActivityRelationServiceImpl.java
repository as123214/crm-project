package com.jgc.crm.workbench.service.Impl;

import com.jgc.crm.workbench.domain.ClueActivityRelation;
import com.jgc.crm.workbench.mapper.ClueActivityRelationMapper;
import com.jgc.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Override
    public int saveClueRelation(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertClueRelation(list);
    }

    @Override
    public int deleteClueRelation(ClueActivityRelation clueActivityRelation) {
        return clueActivityRelationMapper.deleteClueRelation(clueActivityRelation);
    }
}
