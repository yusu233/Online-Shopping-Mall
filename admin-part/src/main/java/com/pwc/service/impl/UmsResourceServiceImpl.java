package com.pwc.service.impl;

import com.github.pagehelper.PageHelper;
import com.pwc.mapper.UmsResourceMapper;
import com.pwc.model.UmsResource;
import com.pwc.model.UmsResourceExample;
import com.pwc.service.UmsAdminCacheService;
import com.pwc.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UmsResourceServiceImpl implements UmsResourceService {
    @Autowired
    private UmsResourceMapper umsResourceMapper;
    @Autowired
    private UmsAdminCacheService umsAdminCacheService;

    @Override
    public int create(UmsResource umsResource) {
        umsResource.setCreateTime(new Date());
        return umsResourceMapper.insert(umsResource);
    }

    @Override
    public int update(Long id, UmsResource umsResource) {
        umsResource.setId(id);
        int count = umsResourceMapper.updateByPrimaryKeySelective(umsResource);
        umsAdminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public UmsResource getItem(Long id) {
        return umsResourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        int count = umsResourceMapper.deleteByPrimaryKey(id);
        umsAdminCacheService.delResourceListByResource(id);
        return count;
    }

    @Override
    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsResourceExample example = new UmsResourceExample();
        if(categoryId != null){
            example.createCriteria().andCategoryIdEqualTo(categoryId);
        }
        if(nameKeyword != null){
            example.createCriteria().andNameLike("%" + nameKeyword + "%");
        }
        if(urlKeyword != null){
            example.createCriteria().andUrlLike("%" + urlKeyword + "%");
        }
        return umsResourceMapper.selectByExample(example);
    }

    @Override
    public List<UmsResource> listAll() {
        return umsResourceMapper.selectByExample(new UmsResourceExample());
    }
}
