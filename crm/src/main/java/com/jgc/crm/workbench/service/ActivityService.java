package com.jgc.crm.workbench.service;

import com.jgc.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionFromPageNo(Map<String,Object> map);

    int selectCountOfActivityCondition(Map<String,Object> map);

    int deleteActivityByIds(String[] id);

    Activity queryActivityById(String id);

    int saveEditActivity(Activity activity);

    List<Activity> queryAllActivity();

    List<Activity> queryActivityByIds(String[] id);

    int saveActivityByFile(List<Activity> activityList);

    Activity queryActivityForDetailById(String id);

    List<Activity> queryActivityForClueId(String id);

    List<Activity> queryActivityForDetailByNameClueId(Map<String,Object> map);

    List<Activity> queryActivityFroDetailByIds(String[] id);
}
