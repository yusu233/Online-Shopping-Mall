package com.pwc.dao;

import com.pwc.model.SmsCouponProductRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsCouponProductRelationDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<SmsCouponProductRelation> productRelationList);
}