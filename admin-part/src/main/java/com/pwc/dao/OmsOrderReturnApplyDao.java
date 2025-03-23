package com.pwc.dao;

import com.pwc.dto.OmsOrderReturnApplyResult;
import com.pwc.dto.OmsReturnApplyQueryParam;
import com.pwc.model.OmsOrderReturnApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmsOrderReturnApplyDao {
    /**
     * 查询申请列表
     */
    //TODO sql语句存疑
    List<OmsOrderReturnApply> getList(@Param("queryParam") OmsReturnApplyQueryParam queryParam);

    /**
     * 获取申请详情
     */
    OmsOrderReturnApplyResult getDetail(@Param("id")Long id);
}
