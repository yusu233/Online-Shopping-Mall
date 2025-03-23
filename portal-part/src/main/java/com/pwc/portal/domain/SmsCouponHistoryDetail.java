package com.pwc.portal.domain;

import com.pwc.model.SmsCoupon;
import com.pwc.model.SmsCouponHistory;
import com.pwc.model.SmsCouponProductCategoryRelation;
import com.pwc.model.SmsCouponProductRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class SmsCouponHistoryDetail extends SmsCouponHistory {
    @ApiModelProperty("相关优惠券信息")
    private SmsCoupon coupon;
    @ApiModelProperty("优惠券关联商品")
    private List<SmsCouponProductRelation> productRelationList;
    @ApiModelProperty("优惠券关联商品分类")
    private List<SmsCouponProductCategoryRelation> categoryRelationList;
}
