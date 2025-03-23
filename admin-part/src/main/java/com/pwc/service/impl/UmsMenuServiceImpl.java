package com.pwc.service.impl;

import com.github.pagehelper.PageHelper;
import com.pwc.mapper.UmsMenuMapper;
import com.pwc.model.UmsMenu;
import com.pwc.model.UmsMenuExample;
import com.pwc.service.UmsMenuService;
import com.pwc.wrapper.UmsMenuNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsMenuServiceImpl implements UmsMenuService {
    @Autowired
    private UmsMenuMapper umsMenuMapper;
    @Override
    public int create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        setLevel(umsMenu);
        return umsMenuMapper.insert(umsMenu);
    }

    @Override
    public int update(Long id, UmsMenu umsMenu) {
        umsMenu.setId(id);
        setLevel(umsMenu);
        return umsMenuMapper.updateByPrimaryKeySelective(umsMenu);
    }

    @Override
    public UmsMenu getItem(Long id) {
        return umsMenuMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        return umsMenuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsMenuExample example = new UmsMenuExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        example.setOrderByClause("sort desc");
        return umsMenuMapper.selectByExample(example);
    }

    @Override
    public List<UmsMenuNode> treeList() {
        List<UmsMenu> menuList = umsMenuMapper.selectByExample(new UmsMenuExample());
        List<UmsMenuNode> umsMenuNodes = menuList.stream()
                .filter(umsMenu -> umsMenu.getParentId().equals(0L))
                .map(umsMenu -> convertToUmsMenuNode(umsMenu, menuList))
                .collect(Collectors.toList());
        return umsMenuNodes;
    }

    @Override
    public int updateHidden(Long id, Integer hidden) {
        UmsMenu umsMenu = new UmsMenu();
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        return umsMenuMapper.updateByPrimaryKeySelective(umsMenu);
    }

    private void setLevel(UmsMenu umsMenu) {
        if(umsMenu.getParentId() == 0){
            umsMenu.setLevel(0);
        }else {
            UmsMenu parentMenu = umsMenuMapper.selectByPrimaryKey(umsMenu.getParentId());
            if(parentMenu != null){
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            }
            umsMenu.setLevel(0);
        }
    }

    private UmsMenuNode convertToUmsMenuNode(UmsMenu umsMenu, List<UmsMenu> menuList) {
        UmsMenuNode umsMenuNode = new UmsMenuNode();
        BeanUtils.copyProperties(umsMenu, umsMenuNode);
        List<UmsMenuNode> childrenNode = menuList.stream()
                .filter(menu -> menu.getParentId().equals(umsMenu.getId()))
                .map(menu -> convertToUmsMenuNode(menu, menuList))
                .collect(Collectors.toList());
        umsMenuNode.setChildren(childrenNode);
        return umsMenuNode;
    }
}
