package com.pwc.dao;

import com.pwc.dto.SmsCouponParam;
import org.apache.ibatis.annotations.Param;

public interface SmsCouponDao {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParam getItem(@Param("id") Long id);
}
