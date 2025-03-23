package com.pwc.dao;

import com.pwc.model.PmsProductFullReduction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductFullReductionDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<PmsProductFullReduction> productFullReductionList);
}
