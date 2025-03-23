package com.pwc.dao;

import com.pwc.model.PmsProductVertifyRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductVertifyRecordDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<PmsProductVertifyRecord> list);
}
