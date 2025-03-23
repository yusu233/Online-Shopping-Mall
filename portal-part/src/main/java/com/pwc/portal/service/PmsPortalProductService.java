package com.pwc.portal.service;

import com.pwc.model.PmsProduct;
import com.pwc.portal.domain.PmsPortalProductDetail;
import com.pwc.portal.domain.PmsProductCategoryNode;

import java.util.List;

public interface PmsPortalProductService {
    /**
     * 综合搜索商品
     */
    List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 以树形结构获取所有商品分类
     */
    List<PmsProductCategoryNode> categoryTreeList();

    /**
     * 获取前台商品详情
     */
    PmsPortalProductDetail detail(Long id);
}
