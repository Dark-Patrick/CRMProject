package com.ncepu.crm.workbench.service;

import com.ncepu.crm.workbench.domain.Tran;
import com.ncepu.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListById(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();

}
