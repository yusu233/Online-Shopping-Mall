package com.pwc.portal.controller;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.PmsProduct;
import com.pwc.portal.domain.PmsPortalProductDetail;
import com.pwc.portal.domain.PmsProductCategoryNode;
import com.pwc.portal.service.PmsPortalProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Api(tags = "PmsPortalProductController")
@Tag(name = "PmsPortalProductController", description = "前台商品管理")
public class PmsPortalProductController {
    @Autowired
    private PmsPortalProductService pmsPortalProductService;

    @ApiOperation(value = "搜索商品")
    @GetMapping(value = "/search")
    public Result<CommonPage<PmsProduct>> search(@RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) Long brandId,
                                                 @RequestParam(required = false) Long productCategoryId,
                                                 @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                 @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                 @RequestParam(required = false, defaultValue = "0") Integer sort) {
        List<PmsProduct> productList = pmsPortalProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return Result.success(CommonPage.restPage(productList));
    }

    @ApiOperation("以树形结构获取所有商品分类")
    @GetMapping(value = "/categoryTreeList")
    public Result<List<PmsProductCategoryNode>> categoryTreeList() {
        List<PmsProductCategoryNode> list = pmsPortalProductService.categoryTreeList();
        return Result.success(list);
    }

    @ApiOperation("获取前台商品详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Result<PmsPortalProductDetail> detail(@PathVariable Long id) {
        PmsPortalProductDetail productDetail = pmsPortalProductService.detail(id);
        return Result.success(productDetail);
    }
}
