package com.pwc.portal.controller;

import com.pwc.common.api.Result;
import com.pwc.model.SmsCoupon;
import com.pwc.model.SmsCouponHistory;
import com.pwc.portal.domain.CartPromotionItem;
import com.pwc.portal.domain.SmsCouponHistoryDetail;
import com.pwc.portal.service.OmsCartItemService;
import com.pwc.portal.service.UmsMemberCouponService;
import com.pwc.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "UmsMemberCouponController")
@Tag(name = "UmsMemberCouponController", description = "用户优惠券管理")
@RequestMapping("/member/coupon")
public class UmsMemberCouponController {
    @Autowired
    private UmsMemberCouponService memberCouponService;
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("领取指定优惠券")
    @PostMapping(value = "/add/{couponId}")
    public Result add(@PathVariable Long couponId) {
        memberCouponService.add(couponId);
        return Result.success(null,"领取成功");
    }

    @ApiOperation("获取会员优惠券历史列表")
    @GetMapping(value = "/listHistory")
    public Result<List<SmsCouponHistory>> listHistory(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistory> couponHistoryList = memberCouponService.listHistory(useStatus);
        return Result.success(couponHistoryList);
    }

    @ApiOperation("获取会员优惠券列表")
    @GetMapping(value = "/list")
    public Result<List<SmsCoupon>> list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCoupon> couponList = memberCouponService.list(useStatus);
        return Result.success(couponList);
    }

    @ApiOperation("获取登录会员购物车的相关优惠券")
    @GetMapping(value = "/list/cart/{type}")
    public Result<List<SmsCouponHistoryDetail>> listCart(@PathVariable Integer type) {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId(), null);
        List<SmsCouponHistoryDetail> couponHistoryList = memberCouponService.listCart(cartPromotionItemList, type);
        return Result.success(couponHistoryList);
    }

    @ApiOperation("获取当前商品相关优惠券")
    @GetMapping(value = "/listByProduct/{productId}")
    public Result<List<SmsCoupon>> listByProduct(@PathVariable Long productId) {
        List<SmsCoupon> couponHistoryList = memberCouponService.listByProduct(productId);
        return Result.success(couponHistoryList);
    }
}