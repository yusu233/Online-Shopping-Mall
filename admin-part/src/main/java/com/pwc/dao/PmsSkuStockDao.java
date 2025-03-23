package com.pwc.dao;

import com.pwc.model.PmsSkuStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsSkuStockDao {
    /**
     * 批量插入操作
     */
    int insertList(@Param("list") List<PmsSkuStock> skuStockList);

    /**
     * 批量插入或替换操作
     */
    int replaceList(@Param("list")List<PmsSkuStock> skuStockList);
}
