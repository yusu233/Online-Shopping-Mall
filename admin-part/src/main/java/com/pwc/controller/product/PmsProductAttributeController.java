package com.pwc.controller.product;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.dto.PmsProductAttributeParam;
import com.pwc.dto.ProductAttrInfo;
import com.pwc.model.PmsProductAttribute;
import com.pwc.service.PmsProductAttributeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {
    @Autowired
    private PmsProductAttributeService pmsProductAttributeService;

    @ApiOperation("查询单个商品属性")
    @GetMapping(value = "/{id}")
    public Result<PmsProductAttribute> getItem(@PathVariable Long id) {
        PmsProductAttribute productAttribute = pmsProductAttributeService.getItem(id);
        return Result.success(productAttribute);
    }

    @ApiOperation("查询商品属性和分类")
    @GetMapping(value = "/attrInfo/{productCategoryId}")
    public Result<List<ProductAttrInfo>> getAttrInfo(@PathVariable(value = "productCategoryId") Long id) {
        List<ProductAttrInfo> productAttrInfo = pmsProductAttributeService.getProductAttrInfo(id);
        return Result.success(productAttrInfo);
    }

    @ApiOperation("新增单个商品属性")
    @PostMapping(value = "/create")
    public Result create(@RequestBody PmsProductAttributeParam pmsProductAttributeParam) {
        int count = pmsProductAttributeService.create(pmsProductAttributeParam);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("删除商品属性")
    @PostMapping(value = "/delete")
    public Result delete(@RequestParam(value = "ids") List<Long> ids) {
        int count = pmsProductAttributeService.delete(ids);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

    @ApiOperation("根据分类查询属性列表或参数列表")
    @GetMapping(value = "/list/{cid}")
    public Result<CommonPage<PmsProductAttribute>> list(@PathVariable Long cid,
                         @RequestParam(value = "type") Integer type,
                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductAttribute> list = pmsProductAttributeService.getList(cid, type, pageSize, pageNum);
        return Result.success(CommonPage.restPage(list));
    }

    @ApiOperation("修改商品属性")
    @PostMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody PmsProductAttributeParam pmsProductAttributeParam) {
        int count = pmsProductAttributeService.update(id, pmsProductAttributeParam);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.failed();
        }
    }

}
