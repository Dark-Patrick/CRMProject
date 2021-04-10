package com.ncepu.crm.workbench.web.controller;

import com.ncepu.crm.settings.domain.DicValue;
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
import com.ncepu.crm.workbench.domain.Clue;
import com.ncepu.crm.workbench.domain.Tran;
import com.ncepu.crm.workbench.service.ActivityService;
import com.ncepu.crm.workbench.service.ClueService;
import com.ncepu.crm.workbench.service.impl.ActivityServiceImpl;
import com.ncepu.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        String path = req.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(req, resp);
        }else if("/workbench/clue/getDic.do".equals(path)){
            getDic(req, resp);
        }else if("/workbench/clue/save.do".equals(path)){
            save(req, resp);
        }else if("/workbench/clue/pageList.do".equals(path)){
            pageList(req, resp);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(req, resp);
        }else if("/workbench/clue/getActivityByClueId.do".equals(path)){
            getActivityByClueId(req, resp);
        }else if("/workbench/clue/unBund.do".equals(path)){
            unBund(req, resp);
        }else if("/workbench/clue/getActivityByNameExcept.do".equals(path)){
            getActivityByNameExcept(req, resp);
        }else if("/workbench/clue/bund.do".equals(path)){
            bund(req, resp);
        }else if("/workbench/clue/getActivityByName.do".equals(path)){
            getActivityByName(req, resp);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(req, resp);
        }
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("取得用户信息列表");

        UserService userService = (UserService)ServiceFactory.getService(new UserServiceImpl());

        List<User> users = userService.getUserList();

        PrintJson.printJsonObj(resp,users);
    }

    @SuppressWarnings("unchecked")
    private void getDic(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("获取字典数据");
        Map<String, List<DicValue>> map = (Map<String, List<DicValue>>) req.getServletContext().getAttribute("DicTypes");
        PrintJson.printJsonObj(resp, map);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("保存线索数据");
        String id = UUIDUtil.getUUID();
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setAppellation(appellation);
        c.setCompany(company);
        c.setContactSummary(contactSummary);
        c.setSource(source);
        c.setState(state);
        c.setOwner(owner);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setNextContactTime(nextContactTime);
        c.setFullname(fullname);
        //框架的重要性。。。。。
        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);
        PrintJson.printJsonFlag(resp, flag);

    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行查询线索信息列表的操作（结和条件查询+分页查询）");
        String pageNoStr = req.getParameter("pageNo");
        String pageSizeStr = req.getParameter("pageSize");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        int skip = (pageNo - 1) * pageSize;

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        PaginationVO<Clue> vo = cs.pageList(pageSize, skip);

        PrintJson.printJsonObj(resp, vo);

    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入线索详情界面");
        String id = req.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = cs.detail(id);
        req.setAttribute("clue", clue);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req, resp);

    }

    private void getActivityByClueId(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行线索与市场活动关联操作");
        String clueId = req.getParameter("clueId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityByClueId(clueId);
        PrintJson.printJsonObj(resp, aList);

    }

    private void unBund(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("解除线索与市场活动关联");
        String id = req.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.unBund(id);
        PrintJson.printJsonFlag(resp, flag);
    }

    private void getActivityByNameExcept(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("查询市场活动列表（根据名称模糊查询，排除已关联市场活动");
        String activityName = req.getParameter("activityName");
        String clueId = req.getParameter("clueId");

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityByNameExcept(activityName, clueId);
        PrintJson.printJsonObj(resp, aList);
    }

    private void bund(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行关联市场活动的操作");
        String cid = req.getParameter("cid");
        String aids[] = req.getParameterValues("aid");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bund(cid, aids);
        PrintJson.printJsonFlag(resp, flag);
    }

    private void getActivityByName(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("查询市场活动列表");
        String activityName = req.getParameter("activityName");
        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityByName(activityName);
        PrintJson.printJsonObj(resp, aList);
    }

    private  void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("执行线索转换操作");
        String clueId = req.getParameter("clueId");
        Tran t = null;

        //接受是否需要创建交易的标记
        String flag = req.getParameter("flag");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        if("true".equals(flag)){
            //接受交易表单中的参数
            t = new Tran();
            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();
            t.setMoney(money);
            t.setName(name);
            t.setStage(stage);
            t.setExpectedDate(expectedDate);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setId(id);
            t.setCreateBy(createBy);
        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1 = cs.convert(clueId, t, createBy);//t 可能为空，所以要单独传入createBy
        if(flag1){
            resp.sendRedirect(req.getContextPath() + "/workbench/clue/index.html");
        }

    }

}
