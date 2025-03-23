package com.pwc.search.controller;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.search.domain.EsProduct;
import com.pwc.search.service.EsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/esProduct")
@Api(value = "EsProductController")
@Tag(name = "EsProductController", description = "商品搜索管理类")
public class EsProductController {
    @Autowired
    private EsProductService esProductService;
    
    @ApiOperation("根据id创建商品")
    @PostMapping("/create/{id}")
    public Result create(@PathVariable Long id) {
        EsProduct esProduct = esProductService.create(id);
        if(esProduct != null) {
            return Result.success(esProduct);
        }
        return Result.failed();
    }
    
    @ApiOperation("根据id删除商品")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        esProductService.delete(id);
        return Result.success(null);
    }
    
    @ApiOperation("批量删除商品")
    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestParam(name = "ids") List<Long> ids) {
        esProductService.delete(ids);
        return Result.success(null);
    }
    
    @ApiOperation("导入所有商品")
    @PostMapping("/importAll")
    public Result importAll() {
        int count = esProductService.importAll();
        return Result.success(count);
    }
    
    @ApiOperation("根据商品id推荐商品")
    @GetMapping("/recommend/{id}")
    public Result<CommonPage<EsProduct>> recommend(@PathVariable Long id,
                            @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                            @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsProduct> products = esProductService.recommend(id, pageNum, pageSize);
        return Result.success(CommonPage.restPage(products));
    }
    
    @ApiOperation("综合搜索商品")
    @GetMapping("search")
    public Result<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Long brandId,
                                                @RequestParam(required = false) Long productCategoryId,
                                                @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                @RequestParam(required = false, defaultValue = "0") Integer sort) {
        Page<EsProduct> products = esProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return Result.success(CommonPage.restPage(products));
    }
    
    @ApiOperation("根据关键词搜索商品")
    @GetMapping("search/simple")
    public Result<CommonPage<EsProduct>> searchSimple(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsProduct> products = esProductService.search(keyword, pageNum, pageSize);
        return Result.success(CommonPage.restPage(products));
    }
}
