package com.pwc.portal.controller;

import com.pwc.common.api.Result;
import com.pwc.model.UmsMember;
import com.pwc.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.security.Principal;
import java.util.Map;

@RestController
@Api(tags = "UmsMemberController")
@Tag(name = "UmsMemberController", description = "会员登录注册管理")
@RequestMapping("/sso")
public class UmsMemberController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("会员注册")
    @PostMapping(value = "/register")
    public Result register(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String telephone,
                                 @RequestParam String authCode) {
        memberService.register(username, password, telephone, authCode);
        return Result.success(null,"注册成功");
    }

    @ApiOperation("会员登录")
    @PostMapping(value = "/login")
    public Result login(@RequestParam String username,
                              @RequestParam String password) {
        String token = memberService.login(username, password);
        if (token == null) {
            return Result.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return Result.success(tokenMap);
    }

    @ApiOperation("获取会员信息")
    @GetMapping(value = "/info")
    public Result info(Principal principal) {
        if(principal==null){
            return Result.unauthorized(null);
        }
        UmsMember member = memberService.getCurrentMember();
        return Result.success(member);
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/getAuthCode")
    public Result getAuthCode(@RequestParam String telephone) {
        String authCode = memberService.generateAuthCode(telephone);
        return Result.success(authCode,"获取验证码成功");
    }

    @ApiOperation("会员修改密码")
    @PostMapping(value = "/updatePassword")
    public Result updatePassword(@RequestParam String telephone,
                                 @RequestParam String password,
                                 @RequestParam String authCode) {
        memberService.updatePassword(telephone,password,authCode);
        return Result.success(null,"密码修改成功");
    }


    @ApiOperation(value = "刷新token")
    @GetMapping(value = "/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = memberService.refreshToken(token);
        if (refreshToken == null) {
            return Result.failed("token已经过期！");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return Result.success(tokenMap);
    }
}
