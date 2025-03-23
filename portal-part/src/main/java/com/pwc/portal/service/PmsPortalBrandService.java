package com.pwc.portal.service;

import com.pwc.common.api.CommonPage;
import com.pwc.model.PmsBrand;
import com.pwc.model.PmsProduct;

import java.util.List;

public interface PmsPortalBrandService {
    /**
     * 分页获取推荐品牌
     */
    List<PmsBrand> recommendList(Integer pageNum, Integer pageSize);

    /**
     * 获取品牌详情
     */
    PmsBrand detail(Long brandId);

    /**
     * 分页获取品牌关联商品
     */
    CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize);
}
