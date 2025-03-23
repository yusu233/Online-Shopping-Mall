package com.pwc.dao;

import com.pwc.model.PmsMemberPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsMemberPriceDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<PmsMemberPrice> memberPriceList);
}
