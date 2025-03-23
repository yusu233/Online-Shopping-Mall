package com.pwc.dao;

import com.pwc.model.PmsProductLadder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductLadderDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<PmsProductLadder> productLadderList);
}
