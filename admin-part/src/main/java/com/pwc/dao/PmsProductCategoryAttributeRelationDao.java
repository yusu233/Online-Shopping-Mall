package com.pwc.dao;

import com.pwc.model.PmsProductCategoryAttributeRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductCategoryAttributeRelationDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<PmsProductCategoryAttributeRelation> productCategoryAttributeRelationList);
}
