package com.pwc.service.impl;

import com.github.pagehelper.PageHelper;
import com.pwc.dao.PmsProductAttributeDao;
import com.pwc.dto.PmsProductAttributeParam;
import com.pwc.dto.ProductAttrInfo;
import com.pwc.mapper.PmsProductAttributeCategoryMapper;
import com.pwc.mapper.PmsProductAttributeMapper;
import com.pwc.model.PmsProductAttribute;
import com.pwc.model.PmsProductAttributeCategory;
import com.pwc.model.PmsProductAttributeExample;
import com.pwc.service.PmsProductAttributeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {
    @Autowired
    private PmsProductAttributeMapper pmsProductAttributeMapper;
    @Autowired
    private PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;
    @Autowired
    private PmsProductAttributeDao pmsProductAttributeDao;

    @Override
    public List<PmsProductAttribute> getList(Long categoryId, Integer type, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.setOrderByClause("sort desc");
        example.createCriteria()
                .andProductAttributeCategoryIdEqualTo(categoryId)
                .andTypeEqualTo(type);
        return pmsProductAttributeMapper.selectByExample(example);
    }

    @Override
    public int create(PmsProductAttributeParam pmsProductAttributeParam) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(pmsProductAttributeParam, pmsProductAttribute);
        int count = pmsProductAttributeMapper.insertSelective(pmsProductAttribute);

        PmsProductAttributeCategory pmsProductAttributeCategory = pmsProductAttributeCategoryMapper.selectByPrimaryKey(pmsProductAttribute.getProductAttributeCategoryId());
        if(pmsProductAttribute.getType() == 0){
            pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount() + 1);
        }else if(pmsProductAttribute.getType() == 1){
            pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount() + 1);
        }
        pmsProductAttributeCategoryMapper.updateByPrimaryKey(pmsProductAttributeCategory);
        return count;
    }

    @Override
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam, pmsProductAttribute);
        pmsProductAttribute.setId(id);
        return pmsProductAttributeMapper.updateByPrimaryKeySelective(pmsProductAttribute);
    }

    @Override
    public PmsProductAttribute getItem(Long id) {
        return pmsProductAttributeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids) {
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.createCriteria().andIdIn(ids);
        PmsProductAttribute pmsProductAttribute = pmsProductAttributeMapper.selectByPrimaryKey(ids.get(0));
        Integer type = pmsProductAttribute.getType();
        PmsProductAttributeCategory pmsProductAttributeCategory = pmsProductAttributeCategoryMapper.selectByPrimaryKey(pmsProductAttribute.getProductAttributeCategoryId());
        int count = pmsProductAttributeMapper.deleteByExample(example);

        if(type == 0){
            if(pmsProductAttributeCategory.getAttributeCount() >= count){
                pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount() - count);
            }else{
                pmsProductAttributeCategory.setAttributeCount(0);
            }
        }else if(type == 1){
            if(pmsProductAttributeCategory.getParamCount() >= count){
                pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount() - count);
            }else{
                pmsProductAttributeCategory.setParamCount(0);
            }
        }
        pmsProductAttributeCategoryMapper.updateByPrimaryKey(pmsProductAttributeCategory);
        return count;
    }

    @Override
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return pmsProductAttributeDao.getProductAttrInfo(productCategoryId);
    }
}
