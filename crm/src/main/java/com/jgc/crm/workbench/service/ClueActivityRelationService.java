package com.jgc.crm.workbench.service;

import com.jgc.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveClueRelation(List<ClueActivityRelation> list);

    int deleteClueRelation(ClueActivityRelation clueActivityRelation);
}
