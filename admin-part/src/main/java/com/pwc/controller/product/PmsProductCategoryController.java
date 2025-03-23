package com.pwc.controller.product;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.dto.PmsProductCategoryParam;
import com.pwc.dto.PmsProductCategoryWithChildrenItem;
import com.pwc.model.PmsProductCategory;
import com.pwc.service.PmsProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productCategory")
@Api(tags = "PmsProductCategoryController")
@Tag(name = "PmsProductCategoryController", description = "商品品类管理")
public class PmsProductCategoryController {
    @Autowired
    private PmsProductCategoryService pmsProductCategoryService;

    @ApiOperation("根据id获取商品分类")
    @GetMapping(value = "/{id}")
    public Result<PmsProductCategory> getItem(@PathVariable Long id) {
        PmsProductCategory productCategory = pmsProductCategoryService.getItem(id);
        return Result.success(productCategory);
    }

    @ApiOperation("添加商品分类")
    @PostMapping(value = "/cerate")
    public Result create(@RequestBody PmsProductCategoryParam pmsProductCategoryParam) {
        int count = pmsProductCategoryService.create(pmsProductCategoryParam);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("根据id删除商品分类")
    @PostMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Long id) {
        int count = pmsProductCategoryService.delete(id);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("分页查询商品分类")
    @GetMapping(value = "/list/{parentId}")
    public Result<CommonPage<PmsProductCategory>> getItem(@PathVariable Long parentId,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductCategory> list = pmsProductCategoryService.getList(parentId, pageSize, pageNum);
        return Result.success(CommonPage.restPage(list));
    }

    @ApiOperation("查询商品分类和子类")
    @GetMapping(value = "/list/withChildren")
    public Result<List<PmsProductCategoryWithChildrenItem>> getChild() {
        List<PmsProductCategoryWithChildrenItem> pmsProductCategoryWithChildrenItems = pmsProductCategoryService.listWithChildren();
        return Result.success(pmsProductCategoryWithChildrenItems);
    }

    @ApiOperation("修改商品分类")
    @PostMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id,
                               @Validated
                               @RequestBody PmsProductCategoryParam productCategoryParam) {
        int count = pmsProductCategoryService.update(id, productCategoryParam);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("修改导航栏显示状态")
    @PostMapping(value = "/update/navStatus")
    public Result updateNavStatus(@RequestParam("ids") List<Long> ids, @RequestParam("navStatus") Integer navStatus) {
        int count = pmsProductCategoryService.updateNavStatus(ids, navStatus);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("修改显示状态")
    @PostMapping(value = "/update/showStatus")
    public Result updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("navStatus") Integer showStatus) {
        int count = pmsProductCategoryService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

}
