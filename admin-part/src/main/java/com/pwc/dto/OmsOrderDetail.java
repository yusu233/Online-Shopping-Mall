package com.pwc.dto;

import com.pwc.model.OmsOrder;
import com.pwc.model.OmsOrderItem;
import com.pwc.model.OmsOrderOperateHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OmsOrderDetail extends OmsOrder {
    @Getter
    @Setter
    @ApiModelProperty("订单商品列表")
    private List<OmsOrderItem> orderItemList;
    @Getter
    @Setter
    @ApiModelProperty("订单操作记录列表")
    private List<OmsOrderOperateHistory> historyList;
}
