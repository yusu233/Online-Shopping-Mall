package com.pwc.controller.product;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.dto.PmsProductAttributeCategoryItem;
import com.pwc.model.PmsProductAttributeCategory;
import com.pwc.service.PmsProductAttributeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productAttribute/category")
@Api(tags = "PmsProductAttributeCategoryController")
@Tag(name = "PmsProductAttributeCategoryController", description = "商品属性分类管理")
public class PmsProductAttributeCategoryController {
    @Autowired
    private PmsProductAttributeCategoryService pmsProductAttributeCategoryService;

    @ApiOperation("获取单个商品属性分类信息")
    @GetMapping(value = "/{id}")
    public Result<PmsProductAttributeCategory> getItem(@PathVariable Long id) {
        PmsProductAttributeCategory productAttributeCategory = pmsProductAttributeCategoryService.getItem(id);
        return Result.success(productAttributeCategory);
    }

    @ApiOperation("增加商品属性分类信息")
    @PostMapping(value = "/create")
    public Result create(@RequestParam String name) {
        int count = pmsProductAttributeCategoryService.create(name);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("删除指定id的商品属性分类信息")
    @GetMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Long id) {
        int count = pmsProductAttributeCategoryService.delete(id);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("分页查询商品属性分类信息")
    @GetMapping(value = "/list")
    public Result<CommonPage<PmsProductAttributeCategory>> list(@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductAttributeCategory> list = pmsProductAttributeCategoryService.getList(pageSize, pageNum);
        return Result.success(CommonPage.restPage(list));
    }

    @ApiOperation("查询商品属性及子属性分类信息")
    @GetMapping(value = "/list/withAttr")
    public Result<List<PmsProductAttributeCategoryItem>> listWithAttr() {
        List<PmsProductAttributeCategoryItem> listWithAttr = pmsProductAttributeCategoryService.getListWithAttr();
        return Result.success(listWithAttr);
    }

    @ApiOperation("修改商品属性分类信息")
    @GetMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id, @RequestParam String name) {
        int count = pmsProductAttributeCategoryService.update(id, name);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }
}
