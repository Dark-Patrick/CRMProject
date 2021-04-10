package com.ncepu.crm.workbench.web.controller;

import com.ncepu.crm.settings.domain.User;
import com.ncepu.crm.utils.DateTimeUtil;
import com.ncepu.crm.utils.PrintJson;
import com.ncepu.crm.utils.ServiceFactory;
import com.ncepu.crm.utils.UUIDUtil;
import com.ncepu.crm.workbench.domain.Customer;
import com.ncepu.crm.workbench.domain.Tran;
import com.ncepu.crm.workbench.domain.TranHistory;
import com.ncepu.crm.workbench.service.CustomerService;
import com.ncepu.crm.workbench.service.TranService;
import com.ncepu.crm.workbench.service.impl.CustomerServiceImpl;
import com.ncepu.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到交易控制器");
        String path = req.getServletPath();

        if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(req, resp);
        }else if("/workbench/transaction/getStage2P.do".equals(path)){
            getStage2P(req, resp);
        }else if("/workbench/transaction/save.do".equals(path)){
            save(req, resp);
        }else if("/workbench/transaction/detail.do".equals(path)){
            detail(req, resp);
        }else if("/workbench/transaction/getHistoryListById.do".equals(path)){
            getHistoryListById(req, resp);
        }else if("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(req, resp);
        }else if("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(req, resp);
        }
    }

    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("取得客户名称列表（按照客户名称模糊查询）");
        String name = req.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> sList = cs.getCustomerName(name);
        PrintJson.printJsonObj(resp, sList);
    }

    @SuppressWarnings("unchecked")
    private void getStage2P(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("获取Stage2Possibility字典数据");
        Map<String, String> pMap = (Map<String, String>)req.getServletContext().getAttribute("pMap");
        PrintJson.printJsonObj(resp, pMap);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("执行添加交易的操作");
        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String money = req.getParameter("money");
        String name = req.getParameter("name");
        String expectedDate = req.getParameter("expectedDate");
        String customerName = req.getParameter("customerName"); //此处暂时只有客户名称，无ID
        String stage = req.getParameter("stage");
        String type = req.getParameter("transactionType");
        String source = req.getParameter("source");
        String activityId = req.getParameter("activityId");
        String contactsId = req.getParameter("contactsId");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService tranService = (TranService)ServiceFactory.getService(new TranServiceImpl());
        boolean flag = tranService.save(t, customerName);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/workbench/transaction/index.html");
        }
    }

    @SuppressWarnings("unchecked")
    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");
        String id = req.getParameter("id");

        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());
        Tran tran = ts.detail(id);
        //处理可能性
        String stage = tran.getStage();
        Map<String, String> pMap = (Map<String, String>)this.getServletContext().getAttribute("pMap");
        //ServletContext application = req.getServletContext();
        String possibility = pMap.get(stage);

        req.setAttribute("t", tran);
        req.setAttribute("possibility", possibility);
        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void getHistoryListById(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("根据交易ID取得交易历史");
        String tranId = req.getParameter("tranId");
        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList = ts.getHistoryListById(tranId);
        Map<String, String> pMap = (Map<String, String>)this.getServletContext().getAttribute("pMap");
        for(TranHistory th : thList){
            String stage = th.getStage();
            th.setPossibility(pMap.get(stage));
        }
        PrintJson.printJsonObj(resp, thList);
    }

    private void changeStage(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("执行改变阶段操作");

        String id = req.getParameter("id");
        String stage = req.getParameter("stage");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("t", t);
        PrintJson.printJsonObj(resp, map);
    }

    private void getCharts(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("取得交易阶段数量");
        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());
        Map<String, Object> map = ts.getCharts();//已有VO类，但属性中dataList类型为List<T>，我们需要的是List<Map>，利用泛型处理比较麻烦，暂时用Map处理
        PrintJson.printJsonObj(resp, map);
    }

}
