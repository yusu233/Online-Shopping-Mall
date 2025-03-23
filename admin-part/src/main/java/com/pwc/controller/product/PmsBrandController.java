package com.pwc.controller.product;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.dto.PmsBrandParam;
import com.pwc.model.PmsBrand;
import com.pwc.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
@Api(tags = "PmsBrandController")
@Tag(name = "PmsBrandController", description = "后台品牌管理")
public class PmsBrandController {
    @Autowired
    private PmsBrandService pmsBrandService;

    @ApiOperation(value = "根据编号查询品牌信息")
    @GetMapping(value = "/{id}")
    public Result<PmsBrand> getItem(@PathVariable("id") Long id) {
        return Result.success(pmsBrandService.getBrand(id));
    }

    @ApiOperation(value = "添加品牌信息")
    @PostMapping(value = "/create")
    public Result create(@Validated @RequestBody PmsBrandParam pmsBrandParam) {
        int count = pmsBrandService.createBrand(pmsBrandParam);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "删除品牌信息")
    @GetMapping(value = "/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        int count = pmsBrandService.deleteBrand(id);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "批量删除品牌信息")
    @PostMapping(value = "/delete/batch")
    public Result deleteBatch(@RequestParam(value = "ids") List<Long> ids) {
        int count = pmsBrandService.deleteBrand(ids);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "根据名称获取品牌信息")
    @GetMapping(value = "/list")
    public Result<CommonPage<PmsBrand>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "showStatus",required = false) Integer showStatus,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        List<PmsBrand> pmsBrands = pmsBrandService.listBrand(keyword, showStatus, pageNum, pageSize);
        return Result.success(CommonPage.restPage(pmsBrands));
    }


    @ApiOperation(value = "获取全部品牌信息")
    @GetMapping(value = "/listAll")
    public Result<List<PmsBrand>> listAll() {
        List<PmsBrand> pmsBrands = pmsBrandService.listAllBrand();
        return Result.success(pmsBrands);
    }

    @ApiOperation(value = "更新品牌信息")
    @PostMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id, @Validated @RequestBody PmsBrandParam pmsBrandParam) {
        int count = pmsBrandService.updateBrand(id, pmsBrandParam);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "更新生产商信息")
    @PostMapping(value = "/update/factoryStatus")
    public Result updateFactoryStatus(@RequestParam("ids") List<Long> ids, @RequestParam("factoryStatus") Integer factoryStatus) {
        int count = pmsBrandService.updateFactoryStatus(ids, factoryStatus);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation(value = "更新显示信息")
    @PostMapping(value = "/update/showStatus")
    public Result updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("showStatus") Integer showStatus) {
        int count = pmsBrandService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

}
