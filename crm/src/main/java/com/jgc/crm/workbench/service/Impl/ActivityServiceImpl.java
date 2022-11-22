package com.jgc.crm.workbench.service.Impl;

import com.jgc.crm.workbench.domain.Activity;
import com.jgc.crm.workbench.mapper.ActivityMapper;
import com.jgc.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override
    public int saveCreateActivity(Activity activity) {
        int num = activityMapper.insert(activity);

        return num;
    }

    @Override
    public List<Activity> queryActivityByConditionFromPageNo(Map<String, Object> map) {

        return activityMapper.queryActivityByConditionFromPageNo(map);
    }

    @Override
    public int selectCountOfActivityCondition(Map<String, Object> map) {

        return activityMapper.selectCountOfActivityCondition(map);

    }

    @Override
    public int deleteActivityByIds(String[] id) {
        return activityMapper.deleteActivityByIds(id);
    }

    @Override
    public Activity queryActivityById(String id) {

        return activityMapper.queryActivityById(id);
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateActivityById(activity);
    }

    @Override
    public List<Activity> queryAllActivity() {
        return activityMapper.selectAllActivity();
    }

    @Override
    public List<Activity> queryActivityByIds(String[] id) {
        return activityMapper.selectActivityByIds(id);
    }

    @Override
    public int saveActivityByFile(List<Activity> activityList) {

        return activityMapper.insertActivityByFile(activityList);
    }

    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }

    @Override
    public List<Activity> queryActivityForClueId(String id) {
        return activityMapper.selectActivityForClueId(id);
    }

    @Override
    public List<Activity> queryActivityForDetailByNameClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByNameClueId(map);
    }

    @Override
    public List<Activity> queryActivityFroDetailByIds(String[] id) {
        return activityMapper.selectActivityByIds(id);
    }
}
