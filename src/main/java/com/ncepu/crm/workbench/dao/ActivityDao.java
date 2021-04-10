package com.ncepu.crm.workbench.dao;

import com.ncepu.crm.settings.domain.User;
import com.ncepu.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int save(Activity activity);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityByClueId(String clueId);

    List<Activity> getActivityByNameExcept(@Param("activityName") String activityName, @Param("clueId") String clueId);

    List<Activity> getActivityByName(String activityName);

}
