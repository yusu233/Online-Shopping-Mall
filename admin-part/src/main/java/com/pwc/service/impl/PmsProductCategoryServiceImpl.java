package com.pwc.service.impl;

import com.github.pagehelper.PageHelper;
import com.pwc.dao.PmsProductCategoryAttributeRelationDao;
import com.pwc.dao.PmsProductCategoryDao;
import com.pwc.dto.PmsProductCategoryParam;
import com.pwc.dto.PmsProductCategoryWithChildrenItem;
import com.pwc.mapper.PmsProductCategoryAttributeRelationMapper;
import com.pwc.mapper.PmsProductCategoryMapper;
import com.pwc.mapper.PmsProductMapper;
import com.pwc.model.*;
import com.pwc.service.PmsProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {
    @Autowired
    private PmsProductCategoryMapper pmsProductCategoryMapper;
    @Autowired
    private PmsProductCategoryDao pmsProductCategoryDao;
    @Autowired
    private PmsProductMapper pmsProductMapper;
    @Autowired
    private PmsProductCategoryAttributeRelationMapper pmsProductCategoryAttributeRelationMapper;
    @Autowired
    private PmsProductCategoryAttributeRelationDao pmsProductCategoryAttributeRelationDao;


    @Override
    public int create(PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        BeanUtils.copyProperties(pmsProductCategoryParam, pmsProductCategory);
        setCategoryLevel(pmsProductCategory);
        pmsProductCategory.setProductCount(0);
        int count = pmsProductCategoryMapper.insertSelective(pmsProductCategory);
        List<Long> productAttributeIdList = pmsProductCategoryParam.getProductAttributeIdList();
        if(!CollectionUtils.isEmpty(productAttributeIdList)){
            insertRelationList(pmsProductCategory.getId(), productAttributeIdList);
        }
        return count;
    }

    @Override
    public int update(Long id, PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        BeanUtils.copyProperties(pmsProductCategoryParam, pmsProductCategory);
        setCategoryLevel(pmsProductCategory);
        pmsProductCategory.setProductCount(0);

        PmsProduct product = new PmsProduct();
        product.setProductCategoryName(pmsProductCategory.getName());
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andProductCategoryIdEqualTo(id);
        pmsProductMapper.updateByExampleSelective(product,example);

        if(!CollectionUtils.isEmpty(pmsProductCategoryParam.getProductAttributeIdList())){
            PmsProductCategoryAttributeRelationExample relationExample = new PmsProductCategoryAttributeRelationExample();
            relationExample.createCriteria().andProductCategoryIdEqualTo(id);
            pmsProductCategoryAttributeRelationMapper.deleteByExample(relationExample);
            insertRelationList(id,pmsProductCategoryParam.getProductAttributeIdList());
        }else{
            PmsProductCategoryAttributeRelationExample relationExample = new PmsProductCategoryAttributeRelationExample();
            relationExample.createCriteria().andProductCategoryIdEqualTo(id);
            pmsProductCategoryAttributeRelationMapper.deleteByExample(relationExample);
        }
        return pmsProductCategoryMapper.updateByPrimaryKeySelective(pmsProductCategory);

    }

    @Override
    public List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        return pmsProductCategoryMapper.selectByExample(example);
    }

    @Override
    public int delete(Long id) {
        return pmsProductCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return pmsProductCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        return 0;
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andIdIn(ids);
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        pmsProductCategory.setShowStatus(showStatus);
        return pmsProductCategoryMapper.updateByExampleSelective(pmsProductCategory, example);
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return pmsProductCategoryDao.listWithChildren();
    }

    private void setCategoryLevel(PmsProductCategory productCategory) {
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            PmsProductCategory parentCategory = pmsProductCategoryMapper.selectByPrimaryKey(productCategory.getParentId());
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }

    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long productAttrId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttrId);
            relation.setProductCategoryId(productCategoryId);
            relationList.add(relation);
        }
        pmsProductCategoryAttributeRelationDao.insertList(relationList);
    }

}
