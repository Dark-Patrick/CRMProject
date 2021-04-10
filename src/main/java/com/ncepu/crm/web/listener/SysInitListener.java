package com.ncepu.crm.web.listener;

import com.ncepu.crm.settings.domain.DicValue;
import com.ncepu.crm.settings.service.DicService;
import com.ncepu.crm.settings.service.impl.DicServiceImpl;
import com.ncepu.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("服务器缓存数据字典");
        ServletContext application = sce.getServletContext();
        //取数据字典
        //对于数据字典，一定要分门别类的保存；按照typeCode进行分类

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());


        Map<String, List<DicValue>> map = ds.getAll();
        application.setAttribute("DicTypes", map);
//        Set<String> set = map.keySet();
//        for(String key : set){
//            application.setAttribute(key, map.get(key));


        //处理properties
        Map<String, String> pMap = new HashMap<String, String>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration <String> myKeys = rb.getKeys();
        while (myKeys.hasMoreElements()){
            //阶段
            String key = myKeys.nextElement();
            //可能性
            String value = rb.getString(key);

            pMap.put(key, value);
        }
        //将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("服务器清除数据字典");


    }
}
