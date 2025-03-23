package com.pwc.controller.product;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.dto.PmsProductParam;
import com.pwc.dto.PmsProductQueryParam;
import com.pwc.dto.PmsProductResult;
import com.pwc.model.PmsProduct;
import com.pwc.service.PmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@Api(tags = "PmsProductController")
@Tag(name = "PmsProductController", description = "后台商品管理")
public class PmsProductController {
    @Autowired
    private PmsProductService pmsProductService;

    @ApiOperation("创建商品")
    @PostMapping(value = "/create")
    public Result create(@RequestBody PmsProductParam productParam) {
        int count = pmsProductService.create(productParam);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("查询商品")
    @GetMapping(value = "/list")
    public Result<CommonPage<PmsProduct>> list(PmsProductQueryParam productQueryParam,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = pmsProductService.list(productQueryParam, pageSize, pageNum);
        return Result.success(CommonPage.restPage(productList));
    }

    @ApiOperation("模糊查询")
    @GetMapping(value = "/simpleList")
    public Result<List<PmsProduct>> list(String keyword){
        List<PmsProduct> productList = pmsProductService.list(keyword);
        return Result.success(productList);
    }

    @ApiOperation("更新商品信息")
    @PostMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody PmsProductParam productParam) {
        int count = pmsProductService.update(id, productParam);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("批量更新商品删除状态")
    @PostMapping(value = "/update/deleteStatus")
    public Result updateDeleteStatus(@RequestParam("ids") List<Long> ids, @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = pmsProductService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("批量更新商品新品")
    @PostMapping(value = "/update/newStatus")
    public Result updateNewStatus(@RequestParam("ids") List<Long> ids, @RequestParam("newStatus") Integer newStatus) {
        int count = pmsProductService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("批量更新商品上架下架信息")
    @PostMapping(value = "/update/publishStatus")
    public Result updatePublishStatus(@RequestParam("ids") List<Long> ids, @RequestParam("publishStatus") Integer publishStatus) {
        int count = pmsProductService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("批量更新商品推荐信息")
    @PostMapping(value = "/update/recommendStatus")
    public Result updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = pmsProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("批量更新商品审核信息")
    @PostMapping(value = "/update/verifyStatus")
    public Result updateVerifyStatus(@RequestParam("ids") List<Long> ids, @RequestParam("verifyStatus") Integer verifyStatus, @RequestParam("detail") String detail) {
        int count = pmsProductService.updateVerifyStatus(ids, verifyStatus, detail);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("根据商品id获取商品修改信息")
    @PostMapping(value = "/updateInfo/{id}")
    public Result<PmsProductResult> listUpdateInfo(@PathVariable Long id) {
        PmsProductResult updateInfo = pmsProductService.getUpdateInfo(id);
        return Result.success(updateInfo);
    }
}
