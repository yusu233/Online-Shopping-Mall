package com.pwc.controller.user;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.UmsMenu;
import com.pwc.service.UmsMenuService;
import com.pwc.wrapper.UmsMenuNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Api(tags = "UmsMenuController")
@Tag(name = "UmsMenuController", description = "后台菜单管理")
public class UmsMenuController {
    @Autowired
    private UmsMenuService umsMenuService;

    @ApiOperation("根据ID获取菜单")
    @GetMapping("{id}")
    public Result<UmsMenu> getMenu(@PathVariable Long id){
        UmsMenu umsMenu = umsMenuService.getItem(id);
        return Result.success(umsMenu);
    }

    @ApiOperation("创建菜单")
    @PostMapping("/create")
    public Result createMenu(@RequestBody UmsMenu umsMenu){
        int count = umsMenuService.create(umsMenu);
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("删除指定id菜单")
    @PostMapping("/delete/{id}")
    public Result deleteMenu(@PathVariable Long id){
        int count = umsMenuService.delete(id);
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("查询指定id菜单")
    @GetMapping("/list/{parentId}")
    public Result<CommonPage<UmsMenu>> listMenu(@PathVariable Long parentId,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum){
        List<UmsMenu> list = umsMenuService.list(parentId, pageSize, pageNum);
        return Result.success(CommonPage.restPage(list));
    }

    @ApiOperation("以树形结构返回所有菜单列表")
    @RequestMapping(value = "/treeList", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<UmsMenuNode>> treeList() {
        List<UmsMenuNode> list = umsMenuService.treeList();
        return Result.success(list);
    }

    @ApiOperation("修改菜单")
    @PostMapping("/update/{id}")
    public Result updateMenu(@PathVariable Long id, @RequestBody UmsMenu umsMenu){
        int count = umsMenuService.update(id, umsMenu);
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("修改菜单是否隐藏")
    @PostMapping("/updateHidden/{id}")
    public Result updateHiddenMenu(@PathVariable Long id, @RequestParam("hidden") Integer hidden){
        int count = umsMenuService.updateHidden(id, hidden);
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }


}
