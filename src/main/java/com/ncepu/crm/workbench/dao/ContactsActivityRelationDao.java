package com.ncepu.crm.workbench.dao;

import com.ncepu.crm.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationDao {

    int save(List<ContactsActivityRelation> contactsActivityRelationList);

}
