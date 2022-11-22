package com.jgc.crm.workbench.service.Impl;

import com.jgc.crm.workbench.mapper.ActivityRemarkMapper;
import com.jgc.crm.workbench.service.ActivityRemarkService;
import com.jgc.crm.workbench.domain.ActivityRemark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("activityRemark")
public class ActivityRemarkImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;
    @Override
    public List<ActivityRemark> queryActivityRemarksByActivityId(String id) {
        return activityRemarkMapper.selectActivityRemarksByActivityId(id);
    }

    @Override
    public int saveActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertActivityRemark(activityRemark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }

    @Override
    public int editActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.updateActivityReamrk(activityRemark);
    }
}
