package com.ncepu.crm.settings.service.impl;

import com.ncepu.crm.settings.dao.DicTypeDao;
import com.ncepu.crm.settings.dao.DicValueDao;
import com.ncepu.crm.settings.domain.DicType;
import com.ncepu.crm.settings.domain.DicValue;
import com.ncepu.crm.settings.service.DicService;
import com.ncepu.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();

        List<DicType> DicTypeList = dicTypeDao.getTypeList();
        for(DicType dt : DicTypeList){
            String code = dt.getCode();
            //根据每一个字典值的类型来取得字典值列表
            List<DicValue> DicValueList = dicValueDao.getListByCode(code);
            map.put(code, DicValueList);
        }

        return map;
    }
}
