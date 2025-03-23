package com.pwc.controller.product;

import com.pwc.common.api.Result;
import com.pwc.model.PmsSkuStock;
import com.pwc.service.PmsSkuStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sku")
@Api(tags = "PmaSkuStockController")
@Tag(name = "PmaSkuStockController", description = "商品sku管理")
public class PmsSkuStockController {
    @Autowired
    private PmsSkuStockService pmsSkuStockService;

    @ApiOperation("根据商品ID及sku编码模糊搜索sku库存")
    @GetMapping(value = "/{pid}")
    public Result<List<PmsSkuStock>> getList(@PathVariable Long pid, @RequestParam(value = "keyword",required = false) String keyword) {
        List<PmsSkuStock> skuStockList = pmsSkuStockService.getList(pid, keyword);
        return Result.success(skuStockList);
    }

    @ApiOperation("批量更新sku库存信息")
    @PostMapping(value ="/update/{pid}")
    public Result update(@PathVariable Long pid,@RequestBody List<PmsSkuStock> skuStockList){
        int count = pmsSkuStockService.update(pid, skuStockList);
        if(count>0){
            return Result.success(count);
        }else{
            return Result.failed();
        }
    }
}
