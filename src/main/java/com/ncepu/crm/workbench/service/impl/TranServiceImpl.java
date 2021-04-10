package com.ncepu.crm.workbench.service.impl;

import com.ncepu.crm.utils.DateTimeUtil;
import com.ncepu.crm.utils.SqlSessionUtil;
import com.ncepu.crm.utils.UUIDUtil;
import com.ncepu.crm.workbench.dao.CustomerDao;
import com.ncepu.crm.workbench.dao.TranDao;
import com.ncepu.crm.workbench.dao.TranHistoryDao;
import com.ncepu.crm.workbench.domain.Customer;
import com.ncepu.crm.workbench.domain.Tran;
import com.ncepu.crm.workbench.domain.TranHistory;
import com.ncepu.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Override
    public boolean save(Tran t, String customerName) {
        /**
         * 交易添加业务：
         *      参数t中，缺少客户主键ID
         *      先处理客户相关的需求
         *          1.判断customerName，根据名称在客户表查询ID
         *          2.如存在，取出ID，不存在，新建客户
         *      执行添加交易的操作
         *      添加交易历史
         */
        boolean flag = true;

        Customer customer = customerDao.getByName(customerName);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(t.getCreateTime());
            customer.setCreateBy(t.getCreateBy());
            customer.setContactSummary(t.getContactSummary());//具体信息的填写按照需求
            customer.setName(customerName);
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            customer.setDescription(t.getDescription());
            int count_cus_add = customerDao.save(customer);
            if(count_cus_add != 1)
                flag = false;
        }

        t.setCustomerId(customer.getId());
        int count_add_tran = tranDao.save(t);
        if(count_add_tran != 1)
            flag = false;

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(t.getId());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setStage(t.getStage());
        tranHistory.setExpectedDate(t.getExpectedDate());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(t.getCreateBy());
        int count_add_tranHistory = tranHistoryDao.save(tranHistory);
        if(count_add_tranHistory != 1)
            flag = false;

        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryListById(String tranId) {
        List<TranHistory> thList = tranHistoryDao.getHistoryListById(tranId);
        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;
        int count_1 = tranDao.changeStage(t);
        if(count_1 != 1)
            flag = false;

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setMoney(t.getMoney());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(t.getEditTime());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());

        int count_2 = tranHistoryDao.save(th);
        if(count_2 != 1)
            flag = false;


        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        //取得total
        int total = tranDao.getTotal();
        //取得dataList
        List<Map<String, Object>> dataList = tranDao.getCharts();
        //打包成Map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("dataList", dataList);
        return map;
    }
}
