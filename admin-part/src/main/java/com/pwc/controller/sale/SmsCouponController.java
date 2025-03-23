package com.pwc.controller.sale;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.dto.SmsCouponParam;
import com.pwc.model.SmsCoupon;
import com.pwc.service.SmsCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "SmsCouponController")
@Tag(name = "SmsCouponController", description = "优惠券管理")
@RequestMapping("/coupon")
public class SmsCouponController {
    @Autowired
    private SmsCouponService couponService;

    @ApiOperation("添加优惠券")
    @PostMapping(value = "/create")
    public Result add(@RequestBody SmsCouponParam couponParam) {
        int count = couponService.create(couponParam);
        if(count>0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("删除优惠券")
    @PostMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Long id) {
        int count = couponService.delete(id);
        if(count>0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("修改优惠券")
    @PostMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id,@RequestBody SmsCouponParam couponParam) {
        int count = couponService.update(id,couponParam);
        if(count>0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("根据优惠券名称和类型分页获取优惠券列表")
    @GetMapping(value = "/list")
    public Result<CommonPage<SmsCoupon>> list(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "type",required = false) Integer type,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsCoupon> couponList = couponService.list(name,type,pageSize,pageNum);
        return Result.success(CommonPage.restPage(couponList));
    }

    @ApiOperation("获取单个优惠券的详细信息")
    @GetMapping(value = "/{id}")
    public Result<SmsCouponParam> getItem(@PathVariable Long id) {
        SmsCouponParam couponParam = couponService.getItem(id);
        return Result.success(couponParam);
    }
}
