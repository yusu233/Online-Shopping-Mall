package com.pwc.portal.dao;

import com.pwc.model.OmsOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PortalOrderItemDao {
    /**
     * 批量插入
     */
    int insertList(@Param("list") List<OmsOrderItem> list);
}
