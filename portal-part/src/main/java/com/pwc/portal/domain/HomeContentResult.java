package com.pwc.portal.domain;

import com.pwc.model.CmsSubject;
import com.pwc.model.PmsBrand;
import com.pwc.model.PmsProduct;
import com.pwc.model.SmsHomeAdvertise;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class HomeContentResult {
    @ApiModelProperty("轮播广告")
    private List<SmsHomeAdvertise> advertiseList;
    @ApiModelProperty("推荐品牌")
    private List<PmsBrand> brandList;
    @ApiModelProperty("当前秒杀场次")
    private HomeFlashPromotion homeFlashPromotion;
    @ApiModelProperty("新品推荐")
    private List<PmsProduct> newProductList;
    @ApiModelProperty("人气推荐")
    private List<PmsProduct> hotProductList;
    @ApiModelProperty("推荐专题")
    private List<CmsSubject> subjectList;
}
