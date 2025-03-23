package com.pwc.dao;

import com.pwc.dto.PmsProductAttributeCategoryItem;

import java.util.List;

public interface PmsProductAttributeCategoryDao {
    /**
     * 获取包含属性的商品属性分类
     */
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
