package com.pwc.dao;

import com.pwc.dto.PmsProductResult;
import org.apache.ibatis.annotations.Param;

//TODO：复杂sql语句
public interface PmsProductDao {
    /**
     * 获取商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);
}
