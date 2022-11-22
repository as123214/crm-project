package com.jgc.crm.workbench.service;

import com.jgc.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
     List<ActivityRemark> queryActivityRemarksByActivityId(String id);

     int saveActivityRemark(ActivityRemark activityRemark);

     int deleteActivityRemarkById(String id);

     int editActivityRemark(ActivityRemark activityRemark);


}
