package com.pwc.controller.user;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.UmsMenu;
import com.pwc.model.UmsResource;
import com.pwc.model.UmsRole;
import com.pwc.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Api(tags = "UmsRoleController")
@Tag(name = "UmsRoleController", description = "后台角色管理")
public class UmsRoleController {
    @Autowired
    private UmsRoleService umsRoleService;

    @ApiOperation("给角色分配菜单")
    @PostMapping(value = "/allocMenu")
    public Result allocMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        int count = umsRoleService.allocMenu(roleId, menuIds);
        if(count > 0)
            return Result.success(count);
        return Result.failed();
    }

    @ApiOperation("给角色分配资源")
    @PostMapping(value = "/allocResource")
    public Result allocResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        int count = umsRoleService.allocResource(roleId, resourceIds);
        if(count > 0)
            return Result.success(count);
        return Result.failed();
    }

    @ApiOperation("创建角色")
    @PostMapping("/create")
    public Result create(@RequestBody UmsRole umsRole) {
        int count = umsRoleService.create(umsRole);
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("删除角色")
    @PostMapping("/delete")
    public Result delete(@RequestParam("ids") List<Long> roleIds) {
        int count = umsRoleService.delete(roleIds);
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }


    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    public Result<CommonPage<UmsRole>> list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsRole> roleList = umsRoleService.list(keyword, pageSize, pageNum);
        return Result.success(CommonPage.restPage(roleList));
    }

    @ApiOperation("获取所有角色信息")
    @GetMapping("/listAll")
    public Result<List<UmsRole>> listAll() {
        List<UmsRole> roleList = umsRoleService.list();
        return Result.success(roleList);
    }

    @ApiOperation("获取角色菜单")
    @GetMapping("/listMenu/{roleId}")
    public Result<List<UmsMenu>> getMenu(@PathVariable Long roleId) {
        List<UmsMenu> menuList = umsRoleService.listMenu(roleId);
        return Result.success(menuList);
    }

    @ApiOperation("获取角色资源")
    @GetMapping("/listResource/{roleId}")
    public Result<List<UmsResource>> getResource(@PathVariable Long roleId) {
        List<UmsResource> resourceList = umsRoleService.listResource(roleId);
        return Result.success(resourceList);
    }

    @ApiOperation("更新角色信息")
    @PostMapping("/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody UmsRole umsRole) {
        int update = umsRoleService.update(id, umsRole);
        if(update > 0){
            return Result.success(update);
        }
        return Result.failed();
    }

    @ApiOperation("更新角色状态")
    @PostMapping("/updateStatus/{id}")
    public Result updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        UmsRole umsRole = new UmsRole();
        umsRole.setStatus(status);
        int update = umsRoleService.update(id, umsRole);
        if(update > 0){
            return Result.success(update);
        }
        return Result.failed();
    }

}
