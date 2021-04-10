package com.ncepu.crm.settings.dao;

import com.ncepu.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    User login(@Param("loginAct") String act, @Param("loginPwd") String pwd);

    List<User> getUserList();
}
