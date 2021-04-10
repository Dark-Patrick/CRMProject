package com.ncepu.crm.workbench.dao;

import com.ncepu.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getByName(String company);

    int save(Customer customer);

    List<String> getCustomerName(String name);

}
