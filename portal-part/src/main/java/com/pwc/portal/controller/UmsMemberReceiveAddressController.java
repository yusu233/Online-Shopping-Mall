package com.pwc.portal.controller;

import com.pwc.common.api.Result;
import com.pwc.model.UmsMemberReceiveAddress;
import com.pwc.portal.service.UmsMemberReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "UmsMemberReceiveAddressController")
@Tag(name = "UmsMemberReceiveAddressController", description = "会员收货地址管理")
@RequestMapping("/member/address")
public class UmsMemberReceiveAddressController {
    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;

    @ApiOperation("添加收货地址")
    @PostMapping(value = "/add")
    public Result add(@RequestBody UmsMemberReceiveAddress address) {
        int count = memberReceiveAddressService.add(address);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("删除收货地址")
    @PostMapping(value = "/delete/{id}")
    public Result delete(@PathVariable Long id) {
        int count = memberReceiveAddressService.delete(id);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("修改收货地址")
    @PostMapping(value = "/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody UmsMemberReceiveAddress address) {
        int count = memberReceiveAddressService.update(id, address);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("显示所有收货地址")
    @GetMapping(value = "/list")
    public Result<List<UmsMemberReceiveAddress>> list() {
        List<UmsMemberReceiveAddress> addressList = memberReceiveAddressService.list();
        return Result.success(addressList);
    }

    @ApiOperation("获取收货地址详情")
    @GetMapping(value = "/{id}")
    public Result<UmsMemberReceiveAddress> getItem(@PathVariable Long id) {
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(id);
        return Result.success(address);
    }
}
