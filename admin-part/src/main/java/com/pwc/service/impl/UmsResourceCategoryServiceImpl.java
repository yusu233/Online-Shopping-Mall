package com.pwc.service.impl;

import com.pwc.mapper.UmsResourceCategoryMapper;
import com.pwc.model.UmsResourceCategory;
import com.pwc.model.UmsResourceCategoryExample;
import com.pwc.service.UmsResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {
    @Autowired
    private UmsResourceCategoryMapper umsResourceCategoryMapper;

    @Override
    public List<UmsResourceCategory> listAll() {
        return umsResourceCategoryMapper.selectByExample(new UmsResourceCategoryExample());
    }

    @Override
    public int create(UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        return umsResourceCategoryMapper.insert(umsResourceCategory);
    }

    @Override
    public int update(Long id, UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setId(id);
        return umsResourceCategoryMapper.updateByPrimaryKeySelective(umsResourceCategory);
    }

    @Override
    public int delete(Long id) {
        return umsResourceCategoryMapper.deleteByPrimaryKey(id);
    }
}
