package com.pwc.controller.user;

import com.pwc.common.api.Result;
import com.pwc.model.UmsResourceCategory;
import com.pwc.service.UmsResourceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resourceCategory")
@Api(tags = "UmsResourceCategoryController")
@Tag(name = "UmsResourceCategoryController", description = "后台资源分类管理")
public class UmsResourceCategoryController {
    @Autowired
    private UmsResourceCategoryService umsresourceCategoryService;

    @ApiOperation("新增资源分类")
    @PostMapping("/create")
    public Result create(@RequestBody UmsResourceCategory umsResourceCategory) {
        int count = umsresourceCategoryService.create(umsResourceCategory);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("根据id删除资源分类")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        int count = umsresourceCategoryService.delete(id);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("查询全部资源分类")
    @GetMapping("/listAll")
    public Result<List<UmsResourceCategory>> listAll() {
        List<UmsResourceCategory> categoryList = umsresourceCategoryService.listAll();
        return Result.success(categoryList);
    }

    @ApiOperation("根据id修改资源分类")
    @PostMapping("/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody UmsResourceCategory umsResourceCategory) {
        int count = umsresourceCategoryService.update(id, umsResourceCategory);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
}
