package com.pwc.controller.order;

import com.pwc.common.api.Result;
import com.pwc.model.OmsOrderSetting;
import com.pwc.service.OmsOrderSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orderSetting")
@Api(tags = "OmsOrderSettingController")
@Tag(name = "OmsOrderSettingController", description = "订单设置管理")
public class OmsOrderSettingController {
    @Autowired
    private OmsOrderSettingService orderSettingService;

    @ApiOperation("获取指定订单设置")
    @GetMapping(value = "/{id}")
    public Result<OmsOrderSetting> getItem(@PathVariable Long id) {
        OmsOrderSetting orderSetting = orderSettingService.getItem(id);
        return Result.success(orderSetting);
    }
    
    @ApiOperation("修改指定订单设置")
    @GetMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody OmsOrderSetting orderSetting) {
        int update = orderSettingService.update(id, orderSetting);
        if(update > 0){
            return Result.success(update);
        }
        return Result.failed();
    }
}
