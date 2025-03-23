package com.pwc.service;

import com.pwc.dto.OmsOrderReturnApplyResult;
import com.pwc.dto.OmsReturnApplyQueryParam;
import com.pwc.dto.OmsUpdateStatusParam;
import com.pwc.model.OmsOrderReturnApply;

import java.util.List;

public interface OmsOrderReturnApplyService {
    /**
     * 分页查询申请
     */
    List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量删除申请
     */
    int delete(List<Long> ids);

    /**
     * 修改指定申请状态
     */
    int updateStatus(Long id, OmsUpdateStatusParam statusParam);

    /**
     * 获取指定申请详情
     */
    OmsOrderReturnApplyResult getItem(Long id);
}
