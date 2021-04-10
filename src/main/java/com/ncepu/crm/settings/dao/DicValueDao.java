package com.ncepu.crm.settings.dao;

import com.ncepu.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);

}
