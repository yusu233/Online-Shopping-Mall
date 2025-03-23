package com.pwc.portal.controller;

import com.pwc.common.api.Result;
import com.pwc.portal.domain.AliPayParam;
import com.pwc.portal.service.AlipayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "AlipayController")
@Tag(name = "AlipayController", description = "支付宝支付相关接口")
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private AlipayService alipayService;

    @ApiOperation("支付宝电脑网站支付")
    @GetMapping(value = "/pay")
    public void pay(AliPayParam aliPayParam, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=" + alipayConfig.getCharset());
        response.getWriter().write(alipayService.pay(aliPayParam));
        response.getWriter().flush();
        response.getWriter().close();
    }

    @ApiOperation("支付宝手机网站支付")
    @GetMapping(value = "/webPay")
    public void webPay(AliPayParam aliPayParam, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=" + alipayConfig.getCharset());
        response.getWriter().write(alipayService.webPay(aliPayParam));
        response.getWriter().flush();
        response.getWriter().close();
    }

    @ApiOperation(value = "支付宝异步回调",notes = "必须为POST请求，执行成功返回success，执行失败返回failure")
    @PostMapping(value = "/notify")
    public String notify(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }
        return alipayService.notify(params);
    }

    @ApiOperation(value = "支付宝统一收单线下交易查询",notes = "订单支付成功返回交易状态：TRADE_SUCCESS")
    @GetMapping(value = "/query")
    public Result<String> query(String outTradeNo, String tradeNo){
        return Result.success(alipayService.query(outTradeNo,tradeNo));
    }
}
