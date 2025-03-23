package com.pwc.portal.dao;

import com.pwc.model.SmsCoupon;
import com.pwc.portal.domain.CartProduct;
import com.pwc.portal.domain.PromotionProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PortalProductDao {
    /**
     * 获取购物车商品信息
     */
    CartProduct getCartProduct(@Param("id") Long id);

    /**
     * 获取促销商品信息列表
     */
    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);

    /**
     * 获取可用优惠券列表
     */
    List<SmsCoupon> getAvailableCouponList(@Param("productId") Long productId, @Param("productCategoryId") Long productCategoryId);
}
