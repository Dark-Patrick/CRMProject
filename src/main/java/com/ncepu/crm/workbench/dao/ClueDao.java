package com.ncepu.crm.workbench.dao;

import com.ncepu.crm.workbench.domain.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueDao {

    int save(Clue c);

    int getTotal();

    List<Clue> getClueByPageSize(@Param("pageSize") int pageSize, @Param("skip") int skip);

    Clue getClueById(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
