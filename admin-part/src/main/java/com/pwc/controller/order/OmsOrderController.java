package com.pwc.controller.order;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.dto.*;
import com.pwc.model.OmsOrder;
import com.pwc.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "OmsOrderController")
@Tag(name = "OmsOrderController", description = "订单管理")
@RequestMapping("/order")
public class OmsOrderController {
    @Autowired
    private OmsOrderService orderService;

    @ApiOperation("获取订单详情")
    @GetMapping(value = "/{id}")
    public Result<OmsOrderDetail> detail(@PathVariable Long id) {
        OmsOrderDetail orderDetailResult = orderService.detail(id);
        return Result.success(orderDetailResult);
    }

    @ApiOperation("删除订单")
    @PostMapping(value = "/delete")
    public Result delete(@RequestParam(value = "ids") List<Long> ids) {
        int count = orderService.delete(ids);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
    
    @ApiOperation("根据输入条件查询订单")
    @GetMapping(value = "/list")
    public Result<CommonPage<OmsOrder>> list(OmsOrderQueryParam queryParam,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrder> list = orderService.list(queryParam, pageSize, pageNum);
        return Result.success(CommonPage.restPage(list));
    }

    @ApiOperation("关闭订单")
    @PostMapping(value = "/update/close")
    public Result close(@RequestParam("ids") List<Long> ids, @RequestParam String note) {
        int count = orderService.close(ids, note);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
    
    @ApiOperation("批量发货")
    @PostMapping(value = "/update/delivery")
    public Result delivery(@RequestBody List<OmsOrderDeliveryParam> deliveryParamList) {
        int count = orderService.delivery(deliveryParamList);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
    
    @ApiOperation("批量发货")
    @PostMapping(value = "/update/moneyInfo")
    public Result moneyInfo(@RequestBody OmsMoneyInfoParam moneyInfoParam) {
        int count = orderService.updateMoneyInfo(moneyInfoParam);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
    
    @ApiOperation("增加订单备注")
    @PostMapping(value = "/update/note")
    public Result note(@RequestParam("id") Long id,
                           @RequestParam("note") String note,
                           @RequestParam("status") Integer status) {
        int count = orderService.updateNote(id, note, status);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
    
    @ApiOperation("增加订单备注")
    @PostMapping(value = "/update/receiverInfo")
    public Result receiverInfo(@RequestBody OmsReceiverInfoParam receiverInfoParam) {
        int count = orderService.updateReceiverInfo(receiverInfoParam);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
    
    
}
