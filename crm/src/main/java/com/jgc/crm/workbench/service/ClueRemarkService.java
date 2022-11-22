package com.jgc.crm.workbench.service;

import com.jgc.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    int saveClueRemark(ClueRemark clueRemark);
    List<ClueRemark> queryAllClueRemarkByClueId(String id);
    int deleteClueRemarkById(String id);
}
