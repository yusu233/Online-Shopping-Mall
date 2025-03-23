package com.pwc.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.pwc.common.service.RedisService;
import com.pwc.dao.UmsAdminRoleRelationDao;
import com.pwc.mapper.UmsAdminRoleRelationMapper;
import com.pwc.model.UmsAdmin;
import com.pwc.model.UmsAdminRoleRelation;
import com.pwc.model.UmsAdminRoleRelationExample;
import com.pwc.model.UmsResource;
import com.pwc.service.UmsAdminCacheService;
import com.pwc.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {
    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Override
    public void delAdmin(Long adminId) {
        UmsAdmin umsAdmin = adminService.getItem(adminId);
        if(umsAdmin != null){
            String key =REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + adminId;
            redisService.del(key);
        }
    }

    @Override
    public void delResourceList(Long adminId) {
        UmsAdmin umsAdmin = adminService.getItem(adminId);
        if(umsAdmin != null){
            String key =REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
            redisService.del(key);
        }
    }

    @Override
    public void delResourceListByRoleId(Long roleId) {
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<UmsAdminRoleRelation> adminRoleRelations = adminRoleRelationMapper.selectByExample(example);
        if(CollUtil.isNotEmpty(adminRoleRelations)){
            String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = adminRoleRelations.stream().map(relation -> key + relation.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            delResourceListByRoleId(roleId);
        }
    }

    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIdList = adminRoleRelationDao.getAdminIdList(resourceId);
        if(CollUtil.isNotEmpty(adminIdList)){
            String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = adminIdList.stream().map(adminId -> key + adminId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public UmsAdmin getAdmin(String username) {
        return (UmsAdmin) redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username);
    }

    @Override
    public void setAdmin(UmsAdmin admin) {
        redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername(), admin, REDIS_EXPIRE);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        return (List<UmsResource>) redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId);
    }

    @Override
    public void setResourceList(Long adminId, List<UmsResource> resourceList) {
        redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId, resourceList, REDIS_EXPIRE);
    }
}
