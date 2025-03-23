package com.pwc.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.pwc.common.api.CommonPage;
import com.pwc.mapper.PmsBrandMapper;
import com.pwc.mapper.PmsProductMapper;
import com.pwc.model.PmsBrand;
import com.pwc.model.PmsProduct;
import com.pwc.model.PmsProductExample;
import com.pwc.portal.dao.HomeDao;
import com.pwc.portal.service.PmsPortalBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PmsPortalBrandServiceImpl implements PmsPortalBrandService {
    @Autowired
    private PmsBrandMapper pmsBrandMapper;
    @Autowired
    private PmsProductMapper pmsProductMapper;
    @Autowired
    private HomeDao homeDao;

    @Override
    public List<PmsBrand> recommendList(Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return homeDao.getRecommendBrandList(offset, pageSize);
    }

    @Override
    public PmsBrand detail(Long brandId) {
        return pmsBrandMapper.selectByPrimaryKey(brandId);
    }

    @Override
    public CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andDeleteStatusEqualTo(0)
                .andPublishStatusEqualTo(1)
                .andBrandIdEqualTo(brandId);
        List<PmsProduct> productList = pmsProductMapper.selectByExample(example);
        return CommonPage.restPage(productList);
    }
}
