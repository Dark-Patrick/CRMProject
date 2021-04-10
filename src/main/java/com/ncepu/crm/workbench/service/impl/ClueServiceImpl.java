package com.ncepu.crm.workbench.service.impl;

import com.ncepu.crm.utils.DateTimeUtil;
import com.ncepu.crm.utils.SqlSessionUtil;
import com.ncepu.crm.utils.UUIDUtil;
import com.ncepu.crm.vo.PaginationVO;
import com.ncepu.crm.workbench.dao.*;
import com.ncepu.crm.workbench.domain.*;
import com.ncepu.crm.workbench.service.ClueService;

import java.util.ArrayList;
import java.util.List;

public class ClueServiceImpl implements ClueService {
    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao activityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Clue c) {
        boolean flag = true;
        int count = clueDao.save(c);
        if(count != 1) flag = false;
        return flag;
    }

    @Override
    public PaginationVO<Clue> pageList(int pageSize, int skip) {
        int total = clueDao.getTotal();

        List<Clue> dataList = clueDao.getClueByPageSize(pageSize, skip);

        PaginationVO<Clue> vo = new PaginationVO<Clue>();
        vo.setDataList(dataList);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.getClueById(id);
        return clue;
    }

    @Override
    public boolean unBund(String id) {
        boolean flag = true;
        int count = activityRelationDao.unBund(id);
        if(count != 1) flag = false;
        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag = true;
        for(String aid : aids){
            //取得每一个aid和cid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);
            //添加关联操作
            int count = activityRelationDao.bund(car);
            if(count != 1) flag = false;
        }
        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        /**
         * 转换实现的步骤
         * 1.获取到线索id，通过id获取线索对象（线索对象当中封装了线索的信息）
         * 2.通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司名称精确匹配，判断该客户是否存在
         * 3.通过线索对象提取联系人信息，保存联系人
         * 4.线索备注转换到客户备注和联系人备注
         * 5.“线索和市场活动”的关系转换到“联系人和市场活动”的关系
         * 6.如果有创建交易需求，创建一条交易
         * 7.如果创建了交易，则创建一条该交易下的交易历史
         * 8.删除线索备注
         * 9.删除线索和市场活动关系
         * 10.删除线索
         */
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        Clue clue = clueDao.getById(clueId);

        String company = clue.getCompany();
        Customer customer = customerDao.getByName(company);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());
            customer.setPhone(clue.getPhone());
            //添加客户
            int count_company = customerDao.save(customer);
            if(count_company != 1)
                flag = false;
        }

        //经过第二步处理后，客户信息已经拥有，将来在处理其他表的时候，如果需要客户id，直接使用customer.getId()
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        //添加联系人
        int count_contacts = contactsDao.save(contacts);
        if(count_contacts != 1)
            flag = false;

        //经过第三步处理，联系人信息已经拥有
        //第四步
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        //取出每一条线索备注
        for(ClueRemark clueRemark : clueRemarkList){
            //取出备注信息（主要转换到客户备注和联系人备注）
            String noteContent = clueRemark.getNoteContent();
            //创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count_customerRemark = customerRemarkDao.save(customerRemark);
            if(count_customerRemark != 1)
                flag = false;

            //创建联系人备注对象，
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count_contactsRemark = contactsRemarkDao.save(contactsRemark);
            if(count_contactsRemark != 1)
                flag = false;
        }

        //第五步
        List<ClueActivityRelation> clueActivityRelations = activityRelationDao.getListByClueId(clueId);
        List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<ContactsActivityRelation>();
        for(ClueActivityRelation clueActivityRelation : clueActivityRelations){
            //遍历市场活动，取出ID
            String activityId = clueActivityRelation.getActivityId();
            //创建联系人与市场活动关联关系对象，让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelationList.add(contactsActivityRelation);
        }
        int count_contactsActivityRelation =  contactsActivityRelationDao.save(contactsActivityRelationList);
        if(count_contactsActivityRelation != contactsActivityRelationList.size()){
            flag = false;
        }

        //第六步
        if(t != null){
            /*
                t对象在controller中已经封装好的信息如下
                    id,money,name,expectedDate,stage,activityId,creatBy,createTime
                接下来可以通过第一步生成的customer对象，取出信息，继续完善对t对象的封装
            */
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(contacts.getId());
            //添加交易
            int count_tran = tranDao.save(t);
            if(count_tran != 1)
                flag = false;


            //第七步
            //一条交易可以对应多条交易历史，一对多关系
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setTranId(t.getId());
            tranHistory.setStage(t.getStage());
            //添加交易历史
            int count_tranHistory = tranHistoryDao.save(tranHistory);
            if(count_tranHistory != 1)
                flag = false;
        }

        //第八步
        for(ClueRemark clueRemark : clueRemarkList){
            int count_deleteClueRemark = clueRemarkDao.delete(clueRemark);
            if(count_deleteClueRemark != 1)
                flag = false;
        }

        //第九步
        for(ClueActivityRelation clueActivityRelation : clueActivityRelations){
            int count_deleteClueActRelation = activityRelationDao.delete(clueActivityRelation);
            if(count_deleteClueActRelation != 1)
                flag = false;
        }

        //第十步
        int count_deleteClue = clueDao.delete(clueId);
        if(count_deleteClue != 1)
            flag = false;

        return flag;
    }
}
