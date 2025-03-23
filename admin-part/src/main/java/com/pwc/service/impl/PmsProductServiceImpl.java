package com.pwc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.pagehelper.PageHelper;
import com.pwc.dao.*;
import com.pwc.dto.PmsProductParam;
import com.pwc.dto.PmsProductQueryParam;
import com.pwc.dto.PmsProductResult;
import com.pwc.mapper.*;
import com.pwc.model.*;
import com.pwc.service.PmsProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PmsProductServiceImpl implements PmsProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);
    @Autowired
    private PmsProductMapper pmsProductMapper;
    @Autowired
    private PmsMemberPriceDao pmsMemberPriceDao;
    @Autowired
    private PmsMemberPriceMapper pmsMemberPriceMapper;
    @Autowired
    private PmsProductLadderDao pmsProductLadderDao;
    @Autowired
    private PmsProductLadderMapper pmsProductLadderMapper;
    @Autowired
    private PmsProductFullReductionDao pmsProductFullReductionDao;
    @Autowired
    private PmsProductFullReductionMapper pmsProductFullReductionMapper;
    @Autowired
    private PmsSkuStockDao pmsSkuStockDao;
    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;
    @Autowired
    private PmsProductAttributeValueDao pmsProductAttributeValueDao;
    @Autowired
    private PmsProductAttributeValueMapper pmsProductAttributeValueMapper;
    @Autowired
    private CmsSubjectProductRelationDao cmsSubjectProductRelationDao;
    @Autowired
    private CmsSubjectProductRelationMapper cmsSubjectProductRelationMapper;
    @Autowired
    private CmsPreferenceAreaProductRelationDao cmsPreferenceAreaProductRelationDao;
    @Autowired
    private CmsPrefrenceAreaProductRelationMapper cmsPrefrenceAreaProductRelationMapper;
    @Autowired
    private PmsProductDao pmsProductDao;
    @Autowired
    private PmsProductVertifyRecordDao pmsProductVertifyRecordDao;

    @Override
    public int create(PmsProductParam productParam) {
        PmsProduct product = new PmsProduct();
        BeanUtils.copyProperties(productParam, product);
        product.setId(null);
        pmsProductMapper.insertSelective(product);
        Long id = product.getId();
        relateAndInsertList(pmsMemberPriceDao, productParam.getMemberPriceList(), id);
        relateAndInsertList(pmsProductLadderDao, productParam.getProductLadderList(), id);
        relateAndInsertList(pmsProductFullReductionDao, productParam.getProductFullReductionList(), id);
        handleSkuStockCode(productParam.getSkuStockList(), id);
        relateAndInsertList(pmsSkuStockDao, productParam.getSkuStockList(), id);
        relateAndInsertList(pmsProductAttributeValueDao, productParam.getProductAttributeValueList(), id);
        relateAndInsertList(cmsSubjectProductRelationDao, productParam.getSubjectProductRelationList(), id);
        relateAndInsertList(cmsPreferenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), id);
        return 1;
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return pmsProductDao.getUpdateInfo(id);
    }

    @Override
    public int update(Long id, PmsProductParam productParam) {
        PmsProduct product = new PmsProduct();
        product.setId(id);
        pmsProductMapper.updateByPrimaryKeySelective(product);

        PmsMemberPriceExample pmsMemberPriceExample = new PmsMemberPriceExample();
        pmsMemberPriceExample.createCriteria().andProductIdEqualTo(id);
        pmsMemberPriceMapper.deleteByExample(pmsMemberPriceExample);
        relateAndInsertList(pmsMemberPriceDao, productParam.getMemberPriceList(), id);

        PmsProductLadderExample pmsProductLadderExample = new PmsProductLadderExample();
        pmsProductLadderExample.createCriteria().andProductIdEqualTo(id);
        pmsProductLadderMapper.deleteByExample(pmsProductLadderExample);
        relateAndInsertList(pmsProductLadderDao, productParam.getProductLadderList(), id);

        PmsProductFullReductionExample pmsProductFullReductionExample = new PmsProductFullReductionExample();
        pmsProductFullReductionExample.createCriteria().andProductIdEqualTo(id);
        pmsProductFullReductionMapper.deleteByExample(pmsProductFullReductionExample);
        relateAndInsertList(pmsProductFullReductionDao, productParam.getProductFullReductionList(), id);

        handleUpdateSkuStockList(id, productParam);
        PmsProductAttributeValueExample pmsProductAttributeValueExample = new PmsProductAttributeValueExample();
        pmsProductAttributeValueExample.createCriteria().andProductIdEqualTo(id);
        pmsProductAttributeValueMapper.deleteByExample(pmsProductAttributeValueExample);
        relateAndInsertList(pmsProductAttributeValueDao, productParam.getProductAttributeValueList(), id);

        CmsSubjectProductRelationExample cmsSubjectProductRelationExample = new CmsSubjectProductRelationExample();
        cmsSubjectProductRelationExample.createCriteria().andProductIdEqualTo(id);
        cmsSubjectProductRelationMapper.deleteByExample(cmsSubjectProductRelationExample);
        relateAndInsertList(cmsSubjectProductRelationDao, productParam.getSubjectProductRelationList(), id);

        CmsPreferenceAreaProductRelationExample cmsPreferenceAreaProductRelationExample = new CmsPreferenceAreaProductRelationExample();
        cmsPreferenceAreaProductRelationExample.createCriteria().andProductIdEqualTo(id);
        cmsPrefrenceAreaProductRelationMapper.deleteByExample(cmsPreferenceAreaProductRelationExample);
        relateAndInsertList(cmsPreferenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), id);
        return 1;
    }

    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample pmsProductExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = pmsProductExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (productQueryParam.getPublishStatus() != null) {
            criteria.andPublishStatusEqualTo(productQueryParam.getPublishStatus());
        }
        if (productQueryParam.getVerifyStatus() != null) {
            criteria.andVerifyStatusEqualTo(productQueryParam.getVerifyStatus());
        }
        if (!StrUtil.isEmpty(productQueryParam.getKeyword())) {
            criteria.andNameLike("%" + productQueryParam.getKeyword() + "%");
        }
        if (!StrUtil.isEmpty(productQueryParam.getProductSn())) {
            criteria.andProductSnEqualTo(productQueryParam.getProductSn());
        }
        if (productQueryParam.getBrandId() != null) {
            criteria.andBrandIdEqualTo(productQueryParam.getBrandId());
        }
        if (productQueryParam.getProductCategoryId() != null) {
            criteria.andProductCategoryIdEqualTo(productQueryParam.getProductCategoryId());
        }
        return pmsProductMapper.selectByExample(pmsProductExample);
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        PmsProductExample pmsProductExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = pmsProductExample.createCriteria();
        PmsProduct product = new PmsProduct();
        product.setVerifyStatus(verifyStatus);
        int count = pmsProductMapper.updateByExampleSelective(product, pmsProductExample);

        ArrayList<PmsProductVertifyRecord> list = new ArrayList<>();
        for (Long id : ids) {
            PmsProductVertifyRecord pmsProductVertifyRecord = new PmsProductVertifyRecord();
            pmsProductVertifyRecord.setProductId(id);
            pmsProductVertifyRecord.setDetail(detail);
            pmsProductVertifyRecord.setStatus(verifyStatus);
            pmsProductVertifyRecord.setCreateTime(new Date());
            list.add(pmsProductVertifyRecord);
        }
        pmsProductVertifyRecordDao.insertList(list);

        return count;
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct product = new PmsProduct();
        product.setPublishStatus(publishStatus);
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andIdIn(ids);
        int count = pmsProductMapper.updateByExampleSelective(product, pmsProductExample);
        return count;
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct record = new PmsProduct();
        record.setRecommandStatus(recommendStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return pmsProductMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setNewStatus(newStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return pmsProductMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct record = new PmsProduct();
        record.setDeleteStatus(deleteStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return pmsProductMapper.updateByExampleSelective(record, example);
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if(!StrUtil.isEmpty(keyword)){
            criteria.andNameLike("%" + keyword + "%");
            productExample.or().andDeleteStatusEqualTo(0).andProductSnLike("%" + keyword + "%");
        }
        return pmsProductMapper.selectByExample(productExample);
    }

    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = dao.getClass().getMethod("insertList", List.class);
            insertList.invoke(dao, dataList);
        } catch (Exception e) {
            LOGGER.warn("创建产品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if(CollectionUtils.isEmpty(skuStockList)) return;
        for(int i =0;i < skuStockList.size(); i++){
            PmsSkuStock skuStock = skuStockList.get(i);
            if(StrUtil.isEmpty(skuStock.getSkuCode())){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", productId));
                //三位索引id
                sb.append(String.format("%03d", i + 1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    private void handleUpdateSkuStockList(Long id, PmsProductParam productParam) {
        List<PmsSkuStock> currSkuList = productParam.getSkuStockList();
        if(CollUtil.isEmpty(currSkuList)){
            PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
            skuStockExample.createCriteria().andProductIdEqualTo(id);
            pmsSkuStockMapper.deleteByExample(skuStockExample);
            return;
        }

        PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
        skuStockExample.createCriteria().andProductIdEqualTo(id);
        List<PmsSkuStock> oriStuList = pmsSkuStockMapper.selectByExample(skuStockExample);
        List<PmsSkuStock> insertSkuList = currSkuList.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        List<PmsSkuStock> updateSkuList = currSkuList.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
        List<Long> updateSkuIds = updateSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
        List<PmsSkuStock> removeSkuList = oriStuList.stream().filter(item -> !updateSkuIds.contains(item.getId())).collect(Collectors.toList());
        handleSkuStockCode(insertSkuList,id);
        handleSkuStockCode(updateSkuList,id);

        if(CollUtil.isNotEmpty(insertSkuList)){
            relateAndInsertList(pmsSkuStockDao, insertSkuList, id);
        }

        if(CollUtil.isNotEmpty(removeSkuList)){
            List<Long> removeSkuIds = removeSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
            PmsSkuStockExample removeExample = new PmsSkuStockExample();
            removeExample.createCriteria().andIdIn(removeSkuIds);
            pmsSkuStockMapper.deleteByExample(removeExample);
        }

        if(CollUtil.isNotEmpty(updateSkuList)){
            for (PmsSkuStock pmsSkuStock : updateSkuList) {
                pmsSkuStockMapper.updateByPrimaryKeySelective(pmsSkuStock);
            }
        }

    }

}
