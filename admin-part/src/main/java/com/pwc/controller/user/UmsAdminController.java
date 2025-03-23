package com.pwc.controller.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.UmsAdmin;
import com.pwc.model.UmsRole;
import com.pwc.pojo.UmsAdminLoginParam;
import com.pwc.pojo.UmsAdminParam;
import com.pwc.pojo.UpdateAdminPasswordParam;
import com.pwc.service.UmsAdminService;
import com.pwc.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@Api(tags = "UmsAdminController")
@Tag(name = "UmsAdminController", description = "后台用户管理")
public class UmsAdminController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsAdminService umsAdminService;
    @Autowired
    private UmsRoleService umsRoleService;

    @PostMapping("/register")
    @ResponseBody
    @ApiOperation(value = "注册")
    public Result<UmsAdmin> register(@Validated @RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = umsAdminService.register(umsAdminParam);
        if(umsAdmin == null) {
            return Result.failed("用户名重复");
        }
        return Result.success(umsAdmin);
    }

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation(value = "登陆")
    public Result login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String token = umsAdminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if(token == null)
            return Result.validateFailed("用户名或密码错误");
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("tokenHead", tokenHead);
        return Result.success(map);
    }
    
    //TODO
    @PostMapping("/logout")
    @ResponseBody
    @ApiOperation(value = "退出登陆")
    public Result logout(){
        return Result.success(null);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation("通过id获取用户信息")
    public Result<UmsAdmin> getUmsAdminById(@PathVariable("id") Long id){
        UmsAdmin umsAdmin = umsAdminService.getItem(id);
        return Result.success(umsAdmin);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation("删除指定用户")
    public Result<UmsAdmin> removeUser(@PathVariable("id") Long id){
        int deleted = umsAdminService.delete(id);
        if(deleted > 0){
            return Result.success(null);
        }
        return Result.failed("删除用户失败");
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    @ApiOperation("修改指定用户信息")
    public Result<UmsAdmin> updateUser(@PathVariable Long id, @RequestBody UmsAdmin umsAdmin){
        int deleted = umsAdminService.update(id, umsAdmin);
        if(deleted > 0){
            return Result.success(null);
        }
        return Result.failed("删除用户失败");
    }

    @PostMapping("/updatePassword")
    @ResponseBody
    @ApiOperation("修改指定用户密码")
    public Result updatePassword(@Validated @RequestBody UpdateAdminPasswordParam updateAdminPasswordParam){
        int state = umsAdminService.updatePassword(updateAdminPasswordParam);
        switch(state){
            case 0:
                return Result.success(state);
            case -1:
                return Result.failed("参数不合法");
            case -2:
                return Result.failed("用户不存在");
            case -3:
                return Result.failed("旧密码错误");
            default:
                return  Result.failed("未知错误");
        }
    }

    @PostMapping("/updateStatus/{id}")
    @ResponseBody
    @ApiOperation("修改指定用户账号状态")
    public Result updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status){
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setStatus(status);
        int update = umsAdminService.update(id, umsAdmin);
        if(update > 0){
            return Result.success(update);
        }
        return Result.failed();
    }


    @GetMapping("/info")
    @ResponseBody
    @ApiOperation("获取当前登陆用户信息")
    public Result getAdminInfo(Principal principal){
        if(principal == null){
            return Result.unauthorized(null);
        }

        String userName = principal.getName();
        UmsAdmin umsAdmin = umsAdminService.getAdminByUsername(userName);
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("menus", umsRoleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = umsAdminService.getRoleList(umsAdmin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roleNameList = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles", roleNameList);
        }

        return Result.success(data);
    }

    @ApiOperation(value = "检索用户列表")
    @GetMapping("/list")
    @ResponseBody
    public Result<CommonPage<UmsAdmin>> list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "pageSize") Integer pageSize,
                       @RequestParam(value = "pageNum") Integer pageNum){
        List<UmsAdmin> umsAdminList = umsAdminService.list(keyword, pageSize, pageNum);
        return Result.success(CommonPage.restPage(umsAdminList));
    }

    @ApiOperation(value = "刷新token")
    @GetMapping("/refreshtoken")
    @ResponseBody
    public Result refreshToken(HttpServletRequest request){
        String token = request.getHeader(this.tokenHeader);
        String newToken = umsAdminService.refreshToken(token);
        if(StrUtil.isEmpty(newToken)){
            return Result.failed("token已过期");
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("token", newToken);
        map.put("tokenHead", tokenHead);
        return Result.success(map);
    }

    @ApiOperation(value = "获取指定用户的角色")
    @GetMapping("/role/{adminId}")
    @ResponseBody
    public Result getRoleList(@PathVariable Long adminId){
        List<UmsRole> roleList = umsAdminService.getRoleList(adminId);
        return Result.success(roleList);
    }

    @ApiOperation(value = "修改用户的角色")
    @PostMapping("/role/update")
    @ResponseBody
    public Result updateRole(@RequestParam("adminId") Long adminId, @RequestBody List<Long> roleIds){
        int count = umsAdminService.updateRole(adminId, roleIds);
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }
}
