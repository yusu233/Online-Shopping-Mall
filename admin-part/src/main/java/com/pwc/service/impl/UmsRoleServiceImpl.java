package com.pwc.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.pwc.dao.UmsRoleDao;
import com.pwc.mapper.UmsRoleMapper;
import com.pwc.mapper.UmsRoleMenuRelationMapper;
import com.pwc.mapper.UmsRoleResourceRelationMapper;
import com.pwc.model.*;
import com.pwc.service.UmsAdminCacheService;
import com.pwc.service.UmsAdminService;
import com.pwc.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UmsRoleServiceImpl implements UmsRoleService {
    @Autowired
    private UmsRoleMapper umsRoleMapper;
    @Autowired
    private UmsAdminCacheService umsAdminCacheService;
    @Autowired
    private UmsRoleDao umsRoleDao;
    @Autowired
    private UmsRoleMenuRelationMapper umsRoleMenuRelationMapper;
    @Autowired
    private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;


    @Override
    public int create(UmsRole role) {
        role.setAdminCount(0);
        role.setCreateTime(new Date());
        role.setSort(0);
        return umsRoleMapper.insert(role);
    }

    @Override
    public int update(Long id, UmsRole role) {
        role.setId(id);
        return umsRoleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int delete(List<Long> ids) {
        UmsRoleExample example = new UmsRoleExample();
        example.createCriteria().andIdIn(ids);
        int i = umsRoleMapper.deleteByExample(example);
        umsAdminCacheService.delResourceListByRoleIds(ids);
        return i;
    }
    
    //TODO
    @Override
    public List<UmsRole> list() {
        umsAdminCacheService.getRoles();
        return umsRoleMapper.selectByExample(new UmsRoleExample());
    }

    @Override
    public List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsRoleExample example = new UmsRoleExample();
        if(!StrUtil.isEmpty(keyword)){
            example.createCriteria().andNameLike("%" + keyword + "%");
        }

        return umsRoleMapper.selectByExample(example);
    }

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return umsRoleDao.getMenuListByAdminId(adminId);
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        return umsRoleDao.getMenuListByRoleId(roleId);
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        return umsRoleDao.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        UmsRoleMenuRelationExample example = new UmsRoleMenuRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleMenuRelationMapper.deleteByExample(example);

        for (Long menuId : menuIds) {
            UmsRoleMenuRelation umsRoleMenuRelation = new UmsRoleMenuRelation();
            umsRoleMenuRelation.setRoleId(roleId);
            umsRoleMenuRelation.setMenuId(menuId);
            umsRoleMenuRelationMapper.insert(umsRoleMenuRelation);
        }

        return menuIds.size();
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        UmsRoleResourceRelationExample example = new UmsRoleResourceRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleResourceRelationMapper.deleteByExample(example);

        for (Long resourceId : resourceIds) {
            UmsRoleResourceRelation umsRoleResourceRelation = new UmsRoleResourceRelation();
            umsRoleResourceRelation.setRoleId(roleId);
            umsRoleResourceRelation.setResourceId(resourceId);
            umsRoleResourceRelationMapper.insert(umsRoleResourceRelation);
        }
        umsAdminCacheService.delResourceListByRoleId(roleId);

        return resourceIds.size();
    }
}
