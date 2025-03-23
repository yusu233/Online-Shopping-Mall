package com.pwc.controller.user;

import com.pwc.common.api.CommonPage;
import com.pwc.common.api.Result;
import com.pwc.model.UmsResource;
import com.pwc.security.component.DynamicSecurityMetadataSource;
import com.pwc.service.UmsResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource")
@Api(tags = "UmsResourceController")
@Tag(name = "UmsResourceController", description = "后台资源管理")
public class UmsResourceController {
    @Autowired
    private UmsResourceService umsResourceService;
    @Autowired
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @ApiOperation("根据id查询资源")
    @GetMapping("/{id}")
    public Result<UmsResource> list(@PathVariable Long id) {
        UmsResource item = umsResourceService.getItem(id);
        return Result.success(item);
    }

    @ApiOperation("新增资源")
    @PostMapping("/create")
    public Result create(@RequestBody UmsResource umsResource) {
        int count = umsResourceService.create(umsResource);
        dynamicSecurityMetadataSource.clearAttributeMap();
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("根据id删除资源")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        int count = umsResourceService.delete(id);
        dynamicSecurityMetadataSource.clearAttributeMap();
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }

    @ApiOperation("查询资源")
    @PostMapping("/list")
    public Result<CommonPage<UmsResource>> listMatchedResources(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                       @RequestParam(value = "nameKeyword", required = false) String nameKeyword,
                                       @RequestParam(value = "urlKeyword", required = false) String urlKeyword,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsResource> umsResourceList = umsResourceService.list(categoryId, nameKeyword, urlKeyword, pageSize, pageNum);
        return Result.success(CommonPage.restPage(umsResourceList));
    }

    @ApiOperation("查询全部资源")
    @GetMapping("/listAll")
    public Result<List<UmsResource>> listAll() {
        List<UmsResource> umsResourceList = umsResourceService.listAll();
        return Result.success(umsResourceList);
    }

    @ApiOperation("修改指定id对应的资源")
    @PostMapping("/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody UmsResource umsResource) {
        int count = umsResourceService.update(id, umsResource);
        dynamicSecurityMetadataSource.clearAttributeMap();
        if(count > 0){
            return Result.success(count);
        }
        return Result.failed();
    }

}
