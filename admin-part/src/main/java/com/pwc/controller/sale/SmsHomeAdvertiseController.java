package com.pwc.controller.sale;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.SmsHomeAdvertise;
import com.pwc.service.SmsHomeAdvertiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "SmsHomeAdvertiseController")
@Tag(name = "SmsHomeAdvertiseController", description = "首页轮播广告管理")
@RequestMapping("/home/advertise")
public class SmsHomeAdvertiseController {
    @Autowired
    private SmsHomeAdvertiseService advertiseService;

    @ApiOperation("添加广告")
    @PostMapping(value = "/create")
    public Result create(@RequestBody SmsHomeAdvertise advertise) {
        int count = advertiseService.create(advertise);
        if (count > 0)
            return Result.success(count);
        return Result.failed();
    }

    @ApiOperation("删除广告")
    @PostMapping(value = "/delete")
    public Result delete(@RequestParam("ids") List<Long> ids) {
        int count = advertiseService.delete(ids);
        if (count > 0)
            return Result.success(count);
        return Result.failed();
    }

    @ApiOperation("修改上下线状态")
    @PostMapping(value = "/update/status/{id}")
    public Result updateStatus(@PathVariable Long id, Integer status) {
        int count = advertiseService.updateStatus(id, status);
        if (count > 0)
            return Result.success(count);
        return Result.failed();
    }

    @ApiOperation("获取广告详情")
    @GetMapping(value = "/{id}")
    public Result<SmsHomeAdvertise> getItem(@PathVariable Long id) {
        SmsHomeAdvertise advertise = advertiseService.getItem(id);
        return Result.success(advertise);
    }

    @ApiOperation("修改广告")
    @PostMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody SmsHomeAdvertise advertise) {
        int count = advertiseService.update(id, advertise);
        if (count > 0)
            return Result.success(count);
        return Result.failed();
    }

    @ApiOperation("分页查询广告")
    @GetMapping(value = "/list")
    public Result<CommonPage<SmsHomeAdvertise>> list(@RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "type", required = false) Integer type,
                                                           @RequestParam(value = "endTime", required = false) String endTime,
                                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeAdvertise> advertiseList = advertiseService.list(name, type, endTime, pageSize, pageNum);
        return Result.success(CommonPage.restPage(advertiseList));
    }
}
