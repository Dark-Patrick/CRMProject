package com.ncepu.crm.settings.service.impl;

import com.ncepu.crm.exception.LoginException;
import com.ncepu.crm.settings.dao.UserDao;
import com.ncepu.crm.settings.domain.User;
import com.ncepu.crm.settings.service.UserService;
import com.ncepu.crm.utils.DateTimeUtil;
import com.ncepu.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {


    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String act, String pwd, String ip) throws LoginException {

        User user = userDao.login(act, pwd);

        if(user == null){throw new LoginException("账号或密码错误");}

        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0){throw new LoginException("账号已失效");}

        String lockState = user.getLockState();
        if("0".equals(lockState)){throw new LoginException("账号已被锁定");}

//        String allowIps = user.getAllowIps();
//        if(!allowIps.contains(ip)){throw new LoginException("未信任地址，拒绝访问");}

        return user;
    }

    public List<User> getUserList() {
        List<User> users = userDao.getUserList();

        return users;
    }
}