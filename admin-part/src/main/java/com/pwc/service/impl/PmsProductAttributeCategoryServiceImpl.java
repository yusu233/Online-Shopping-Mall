package com.pwc.service.impl;

import com.github.pagehelper.PageHelper;
import com.pwc.dao.PmsProductAttributeCategoryDao;
import com.pwc.dto.PmsProductAttributeCategoryItem;
import com.pwc.mapper.PmsProductAttributeCategoryMapper;
import com.pwc.model.PmsProductAttributeCategory;
import com.pwc.model.PmsProductAttributeCategoryExample;
import com.pwc.model.PmsProductCategory;
import com.pwc.model.PmsProductCategoryExample;
import com.pwc.service.PmsProductAttributeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {
    @Autowired
    private PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;
    @Autowired
    private PmsProductAttributeCategoryDao productAttributeCategoryDao;

    @Override
    public int create(String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        return pmsProductAttributeCategoryMapper.insertSelective(productAttributeCategory);
    }

    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        productAttributeCategory.setId(id);
        return pmsProductAttributeCategoryMapper.updateByPrimaryKeySelective(productAttributeCategory);
    }

    @Override
    public int delete(Long id) {
        return pmsProductAttributeCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return pmsProductAttributeCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return pmsProductAttributeCategoryMapper.selectByExample(new PmsProductAttributeCategoryExample());
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryDao.getListWithAttr();
    }
}
