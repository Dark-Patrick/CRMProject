package com.ncepu.crm.workbench.service;

import com.ncepu.crm.vo.PaginationVO;
import com.ncepu.crm.workbench.domain.Clue;
import com.ncepu.crm.workbench.domain.Tran;

public interface ClueService {

    boolean save(Clue c);

    PaginationVO<Clue> pageList(int pageSize, int skip);

    Clue detail(String id);

    boolean unBund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);

}
