package com.pwc.portal.domain;

import com.pwc.model.PmsProduct;
import com.pwc.model.PmsProductAttribute;
import com.pwc.model.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartProduct extends PmsProduct {
    @ApiModelProperty("商品属性列表")
    private List<PmsProductAttribute> productAttributeList;
    @ApiModelProperty("商品SKU库存列表")
    private List<PmsSkuStock> skuStockList;
}
