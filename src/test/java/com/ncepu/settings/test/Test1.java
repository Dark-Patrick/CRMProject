package com.ncepu.settings.test;

import com.ncepu.crm.utils.DateTimeUtil;
import com.ncepu.crm.utils.MD5Util;

public class Test1 {

    public static void main(String[] args) {
        //验证失效时间
        String sysTime = DateTimeUtil.getSysTime();
        String expireTime = "2021-03-19 14:44:14";

        int count = expireTime.compareTo(sysTime);
        System.out.println(sysTime);
        System.out.println(count);

        String a = "秦文涛";
        System.out.println(MD5Util.getMD5(a));

    }
}
