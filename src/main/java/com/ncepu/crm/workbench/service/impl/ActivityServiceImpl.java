package com.ncepu.crm.workbench.service.impl;

import com.ncepu.crm.settings.dao.UserDao;
import com.ncepu.crm.settings.domain.User;
import com.ncepu.crm.utils.SqlSessionUtil;
import com.ncepu.crm.vo.PaginationVO;
import com.ncepu.crm.workbench.dao.ActivityDao;
import com.ncepu.crm.workbench.dao.ActivityRemarkDao;
import com.ncepu.crm.workbench.domain.Activity;
import com.ncepu.crm.workbench.domain.ActivityRemark;
import com.ncepu.crm.workbench.service.ActivityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.save(activity);
        if(count != 1) flag = false;

        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        int total = activityDao.getTotalByCondition(map);

        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count = activityRemarkDao.getCountByAids(ids);
        //删除备注，返回受到影响的条数（实际删除的数量）
        int successCount = activityRemarkDao.deleteByAids(ids);
        if(count != successCount) flag = false;
        //删除市场活动
        int countAid = activityDao.delete(ids);

        if(countAid != ids.length) flag = false;

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndAct(String id) {
        List<User> uList = userDao.getUserList();

        Activity activity = activityDao.getById(id);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uList", uList);
        map.put("activity", activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;

        int count = activityDao.update(activity);
        if(count != 1) flag = false;

        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListById(String activityId) {
        List<ActivityRemark> remarks = activityRemarkDao.getRemarkListById(activityId);
        return remarks;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(id);
        if(count != 1) flag = false;
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(ar);
        if(count != 1) flag = false;
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if(count != 1) flag = false;
        return flag;
    }

    @Override
    public List<Activity> getActivityByClueId(String clueId) {
        List<Activity> aList = activityDao.getActivityByClueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityByNameExcept(String activityName, String clueId) {
        List<Activity> aList = activityDao.getActivityByNameExcept(activityName, clueId);

        return aList;
    }

    @Override
    public List<Activity> getActivityByName(String activityName) {
        List<Activity> aList = activityDao.getActivityByName(activityName);
        return aList;
    }
}
