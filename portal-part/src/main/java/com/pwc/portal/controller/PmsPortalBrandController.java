package com.pwc.portal.controller;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.PmsBrand;
import com.pwc.model.PmsProduct;
import com.pwc.portal.service.PmsPortalBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
@Api(tags = "PmsPortalBrandController")
@Tag(name = "PmsPortalBrandController", description = "前台品牌管理")
public class PmsPortalBrandController {
    @Autowired
    private PmsPortalBrandService pmsPortalBrandService;

    @ApiOperation("分页获取推荐品牌")
    @GetMapping(value = "/recommendList")
    public Result<List<PmsBrand>> recommendList(@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsBrand> brandList = pmsPortalBrandService.recommendList(pageNum, pageSize);
        return Result.success(brandList);
    }

    @ApiOperation("获取品牌详情")
    @GetMapping(value = "/detail/{brandId}")
    public Result<PmsBrand> detail(@PathVariable Long brandId) {
        PmsBrand brand = pmsPortalBrandService.detail(brandId);
        return Result.success(brand);
    }

    @ApiOperation("分页获取品牌相关商品")
    @GetMapping(value = "/productList")
    public Result<CommonPage<PmsProduct>> productList(@RequestParam Long brandId,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        CommonPage<PmsProduct> result = pmsPortalBrandService.productList(brandId,pageNum, pageSize);
        return Result.success(result);
    }
}
