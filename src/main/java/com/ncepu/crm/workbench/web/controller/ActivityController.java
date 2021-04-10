package com.ncepu.crm.workbench.web.controller;

import com.ncepu.crm.settings.domain.User;
import com.ncepu.crm.settings.service.UserService;
import com.ncepu.crm.settings.service.impl.UserServiceImpl;
import com.ncepu.crm.utils.DateTimeUtil;
import com.ncepu.crm.utils.PrintJson;
import com.ncepu.crm.utils.ServiceFactory;
import com.ncepu.crm.utils.UUIDUtil;
import com.ncepu.crm.vo.PaginationVO;
import com.ncepu.crm.workbench.domain.Activity;
import com.ncepu.crm.workbench.domain.ActivityRemark;
import com.ncepu.crm.workbench.service.ActivityService;
import com.ncepu.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        String path = req.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(req, resp);
        }else if("/workbench/activity/save.do".equals(path)){
            save(req, resp);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(req, resp);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(req,resp);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndAct(req, resp);
        }else if("/workbench/activity/update.do".equals(path)){
            update(req, resp);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(req, resp);
        }else if("/workbench/activity/getRemarkListById.do".equals(path)){
            getRemarkListById(req, resp);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(req, resp);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(req, resp);
        }else if("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(req, resp);
        }
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("取得用户信息列表");

        UserService userService = (UserService)ServiceFactory.getService(new UserServiceImpl());

        List<User> users = userService.getUserList();

        PrintJson.printJsonObj(resp,users);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行市场活动添加操作");

        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //创建时间，当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = new Activity();
        activity.setId(id);
        activity.setCost(cost);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        boolean flag = as.save(activity);

        PrintJson.printJsonFlag(resp, flag);


    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行查询市场活动信息列表的操作（结和条件查询+分页查询）");

        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        int skip = (pageNo-1) * pageSize;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner", owner);
        map.put("startDate",startDate);
        map.put("endDate", endDate);
        map.put("skip",skip);
        map.put("pageSize", pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*
            前端要：市场活动信息列表，查询的总条数
            业务层拿到了以上信息后，做返回
            分页查询，每个模块都有，选择使用vo
            PaginationVO<Activity> vo = new PaginationVO<>();
            vo.setTotal(total);
            vo.setDataList(dataList);
            PrintJson.vo-->json
        * */
        PaginationVO<Activity> vo = as.pageList(map);

        PrintJson.printJsonObj(resp,vo);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行市场活动删除操作");
        String[] ids = req.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserListAndAct(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行查询用户信息列表并查询市场活动记录");
        String id = req.getParameter("id");
        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        Map<String, Object> map = as.getUserListAndAct(id);
        PrintJson.printJsonObj(resp, map);

    }

    private void update(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行市场活动更新操作");
        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        //创建时间，当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人，当前登录用户
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = new Activity();
        activity.setId(id);
        activity.setCost(cost);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        boolean flag = as.update(activity);

        PrintJson.printJsonFlag(resp, flag);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("展示市场活动详细信息");
        String id = req.getParameter("id");
        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = as.detail(id);
        req.setAttribute("activity", activity);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req, resp);

    }

    private void getRemarkListById(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("展示市场活动备注信息列表");
        String activityId = req.getParameter("activityId");
        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarks = as.getRemarkListById(activityId);
        PrintJson.printJsonObj(resp, remarks);
    }

    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行删除市场活动备注的操作");
        String id = req.getParameter("id");
        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.deleteRemark(id);
        PrintJson.printJsonFlag(resp, flag);
    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行保存市场活动备注的操作");
        String activityId = req.getParameter("id");
        String noteContent = req.getParameter("noteContent");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setActivityId(activityId);
        ar.setNoteContent(noteContent);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag("0");

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.saveRemark(ar);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);
        PrintJson.printJsonObj(resp, map);
    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行更新市场活动备注操作");
        String id = req.getParameter("id");
        String noteContent = req.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag("1");

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.updateRemark(ar);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);
        PrintJson.printJsonObj(resp, map);
    }
}
