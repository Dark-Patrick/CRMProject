package com.ncepu.crm.settings.service;

import com.ncepu.crm.exception.LoginException;
import com.ncepu.crm.settings.domain.User;
import com.ncepu.crm.vo.PaginationVO;
import com.ncepu.crm.workbench.domain.Clue;

import java.util.List;

public interface UserService {
    User login(String act, String pwd, String ip) throws LoginException;

    List<User> getUserList();

}
