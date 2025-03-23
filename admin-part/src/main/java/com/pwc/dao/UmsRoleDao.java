package com.pwc.dao;

import com.pwc.model.UmsMenu;
import com.pwc.model.UmsResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsRoleDao {
    List<UmsMenu> getMenuListByAdminId(@Param("adminId") Long adminId);
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);
}
