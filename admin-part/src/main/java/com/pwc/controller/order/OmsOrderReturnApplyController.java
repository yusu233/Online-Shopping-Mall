package com.pwc.controller.order;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.dto.OmsOrderReturnApplyResult;
import com.pwc.dto.OmsReturnApplyQueryParam;
import com.pwc.dto.OmsUpdateStatusParam;
import com.pwc.model.OmsOrderReturnApply;
import com.pwc.service.OmsOrderReturnApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "OmsOrderReturnApplyController")
@Tag(name = "OmsOrderReturnApplyController", description = "订单退货申请管理")
@RequestMapping("/returnApply")
public class OmsOrderReturnApplyController {
    @Autowired
    private OmsOrderReturnApplyService orderReturnApplyService;

    @ApiOperation("分页查询退货申请")
    @GetMapping(value = "/list")
    @ResponseBody
    public Result<CommonPage<OmsOrderReturnApply>> list(OmsReturnApplyQueryParam queryParam,
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrderReturnApply> returnApplyList = orderReturnApplyService.list(queryParam, pageSize, pageNum);
        return Result.success(CommonPage.restPage(returnApplyList));
    }

    @ApiOperation("批量删除退货申请")
    @PostMapping(value = "/delete")
    @ResponseBody
    public Result delete(@RequestParam("ids") List<Long> ids) {
        int count = orderReturnApplyService.delete(ids);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("获取退货申请详情")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Result getItem(@PathVariable Long id) {
        OmsOrderReturnApplyResult result = orderReturnApplyService.getItem(id);
        return Result.success(result);
    }

    @ApiOperation("修改退货申请状态")
    @PostMapping(value = "/update/status/{id}")
    @ResponseBody
    public Result updateStatus(@PathVariable Long id, @RequestBody OmsUpdateStatusParam statusParam) {
        int count = orderReturnApplyService.updateStatus(id, statusParam);
        if (count > 0) {
            return Result.success(count);
        }
        return Result.failed();
    }
}
