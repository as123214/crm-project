package com.jgc.crm.workbench.service.Impl;

import com.jgc.crm.workbench.domain.ClueRemark;
import com.jgc.crm.workbench.mapper.ClueRemarkMapper;
import com.jgc.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Override
    public int saveClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertClueRemark(clueRemark);
    }

    @Override
    public List<ClueRemark> queryAllClueRemarkByClueId(String id) {
        return clueRemarkMapper.selectAllClueRemarkByClueId(id);
    }

    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteClueRemarkById(id);
    }
}
