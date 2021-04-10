package com.ncepu.crm.workbench.service;

import com.ncepu.crm.settings.domain.User;
import com.ncepu.crm.vo.PaginationVO;
import com.ncepu.crm.workbench.domain.Activity;
import com.ncepu.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndAct(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListById(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityByClueId(String clueId);

    List<Activity> getActivityByNameExcept(String activityName, String clueId);

    List<Activity> getActivityByName(String activityName);
}
