package com.pwc.portal.controller;

import com.pwc.common.api.Result;
import com.pwc.model.CmsSubject;
import com.pwc.model.PmsProduct;
import com.pwc.model.PmsProductCategory;
import com.pwc.portal.domain.HomeContentResult;
import com.pwc.portal.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "HomeController")
@Tag(name = "HomeController", description = "首页内容管理")
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @ApiOperation("首页内容信息展示")
    @GetMapping(value = "/content")
    public Result<HomeContentResult> content() {
        HomeContentResult contentResult = homeService.content();
        return Result.success(contentResult);
    }

    @ApiOperation("分页获取推荐商品")
    @GetMapping(value = "/recommendProductList")
    public Result<List<PmsProduct>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
        return Result.success(productList);
    }

    @ApiOperation("获取首页商品分类")
    @GetMapping(value = "/productCateList/{parentId}")
    public Result<List<PmsProductCategory>> getProductCateList(@PathVariable Long parentId) {
        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
        return Result.success(productCategoryList);
    }

    @ApiOperation("根据分类获取专题")
    @GetMapping(value = "/subjectList")
    public Result<List<CmsSubject>> getSubjectList(@RequestParam(required = false) Long cateId,
                                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsSubject> subjectList = homeService.getSubjectList(cateId,pageSize,pageNum);
        return Result.success(subjectList);
    }

    @ApiOperation("分页获取人气推荐商品")
    @GetMapping(value = "/hotProductList")
    public Result<List<PmsProduct>> hotProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.hotProductList(pageNum,pageSize);
        return Result.success(productList);
    }

    @ApiOperation("分页获取新品推荐商品")
    @GetMapping(value = "/newProductList")
    public Result<List<PmsProduct>> newProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.newProductList(pageNum,pageSize);
        return Result.success(productList);
    }
}
