package com.pwc.dao;

import com.pwc.dto.ProductAttrInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//TODO select语句有改动
public interface PmsProductAttributeDao {
    /**
     * 获取商品属性信息
     */
    List<ProductAttrInfo> getProductAttrInfo(@Param("id") Long productCategoryId);
}
