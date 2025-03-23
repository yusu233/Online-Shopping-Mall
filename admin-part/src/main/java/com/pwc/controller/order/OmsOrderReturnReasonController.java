package com.pwc.controller.order;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.OmsOrderReturnReason;
import com.pwc.service.OmsOrderReturnReasonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "OmsOrderReturnReasonController")
@Tag(name = "OmsOrderReturnReasonController", description = "退货原因管理")
@RequestMapping("/returnReason")
public class OmsOrderReturnReasonController {
    @Autowired
    private OmsOrderReturnReasonService orderReturnReasonService;

    @ApiOperation("添加退货原因")
    @PostMapping(value = "/create")
    @ResponseBody
    public Result create(@RequestBody OmsOrderReturnReason returnReason) {
        int count = orderReturnReasonService.create(returnReason);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("修改退货原因")
    @PostMapping(value = "/update/{id}")
    @ResponseBody
    public Result update(@PathVariable Long id, @RequestBody OmsOrderReturnReason returnReason) {
        int count = orderReturnReasonService.update(id, returnReason);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("批量删除退货原因")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Result delete(@RequestParam("ids") List<Long> ids) {
        int count = orderReturnReasonService.delete(ids);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("分页查询退货原因")
    @GetMapping(value = "/list")
    @ResponseBody
    public Result<CommonPage<OmsOrderReturnReason>> list(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrderReturnReason> reasonList = orderReturnReasonService.list(pageSize, pageNum);
        return Result.success(CommonPage.restPage(reasonList));
    }

    @ApiOperation("获取单个退货原因详情信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Result<OmsOrderReturnReason> getItem(@PathVariable Long id) {
        OmsOrderReturnReason reason = orderReturnReasonService.getItem(id);
        return Result.success(reason);
    }

    @ApiOperation("修改退货原因启用状态")
    @PostMapping(value = "/update/status")
    @ResponseBody
    public Result updateStatus(@RequestParam(value = "status") Integer status,
                                     @RequestParam("ids") List<Long> ids) {
        int count = orderReturnReasonService.updateStatus(ids, status);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
}
