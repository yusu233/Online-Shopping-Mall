package com.pwc.portal.domain;

import com.pwc.model.OmsOrder;
import com.pwc.model.OmsOrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OmsOrderDetail extends OmsOrder {
    @ApiModelProperty("订单商品列表")
    private List<OmsOrderItem> orderItemList;
}
