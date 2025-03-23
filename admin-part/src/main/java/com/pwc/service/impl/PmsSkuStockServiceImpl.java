package com.pwc.service.impl;

import cn.hutool.core.util.StrUtil;
import com.pwc.dao.PmsSkuStockDao;
import com.pwc.mapper.PmsSkuStockMapper;
import com.pwc.model.PmsSkuStock;
import com.pwc.model.PmsSkuStockExample;
import com.pwc.service.PmsSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;
    @Autowired
    private PmsSkuStockDao pmsSkuStockDao;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(pid);
        if(!StrUtil.isEmpty(keyword)){
            criteria.andSkuCodeLike("%" + keyword + "%");
        }
        return pmsSkuStockMapper.selectByExample(example);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return pmsSkuStockDao.replaceList(skuStockList);
    }
}
