package com.pwc.controller.sale;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.SmsHomeBrand;
import com.pwc.service.SmsHomeBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "SmsHomeBrandController")
@Tag(name = "SmsHomeBrandController", description = "首页品牌管理")
@RequestMapping("/home/brand")
public class SmsHomeBrandController {
    @Autowired
    private SmsHomeBrandService homeBrandService;

    @ApiOperation("添加首页推荐品牌")
    @PostMapping(value = "/create")
    public Result create(@RequestBody List<SmsHomeBrand> homeBrandList) {
        int count = homeBrandService.create(homeBrandList);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("修改推荐品牌排序")
    @PostMapping(value = "/update/sort/{id}")
    public Result updateSort(@PathVariable Long id, Integer sort) {
        int count = homeBrandService.updateSort(id, sort);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("批量删除推荐品牌")
    @PostMapping(value = "/delete")
    public Result delete(@RequestParam("ids") List<Long> ids) {
        int count = homeBrandService.delete(ids);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("批量修改推荐品牌状态")
    @PostMapping(value = "/update/recommendStatus")
    public Result updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = homeBrandService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("分页查询推荐品牌")
    @GetMapping(value = "/list")
    public Result<CommonPage<SmsHomeBrand>> list(@RequestParam(value = "brandName", required = false) String brandName,
                                                       @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeBrand> homeBrandList = homeBrandService.list(brandName, recommendStatus, pageSize, pageNum);
        return Result.success(CommonPage.restPage(homeBrandList));
    }
}