package com.pwc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.pwc.common.exception.Asserts;
import com.pwc.common.util.RequestUtil;
import com.pwc.dao.UmsAdminRoleRelationDao;
import com.pwc.mapper.UmsAdminLoginLogMapper;
import com.pwc.mapper.UmsAdminMapper;
import com.pwc.mapper.UmsAdminRoleRelationMapper;
import com.pwc.mapper.UmsRoleMapper;
import com.pwc.model.*;
import com.pwc.pojo.UmsAdminParam;
import com.pwc.pojo.UpdateAdminPasswordParam;
import com.pwc.security.util.JwtUtil;
import com.pwc.security.util.SpringUtil;
import com.pwc.service.UmsAdminCacheService;
import com.pwc.service.UmsAdminService;
import com.pwc.wrapper.AdminUserDetails;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Autowired
    private UmsAdminMapper umsAdminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UmsAdminLoginLogMapper umsAdminLoginLogMapper;
    @Autowired
    private UmsAdminRoleRelationDao umsAdminRoleRelationDao;
    @Autowired
    private UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);

        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdmins = umsAdminMapper.selectByExample(example);
        if(!umsAdmins.isEmpty()){
            //存在重名
            return null;
        }

        umsAdmin.setPassword(passwordEncoder.encode(umsAdmin.getPassword()));
        umsAdminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        //获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin,resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        //查询redis缓存
        UmsAdmin admin = getCacheService().getAdmin(username);
        if (admin != null) return admin;
        //查询数据库
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(example);
        if (adminList != null && !adminList.isEmpty()) {
            admin = adminList.get(0);
            getCacheService().setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public UmsAdminCacheService getCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }


    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password, userDetails.getPassword())){
                Asserts.fail("密码错误");
            }
            if(!userDetails.isEnabled()){
                Asserts.fail("账户已禁用");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtUtil.generateTokenByUserDetails(userDetails);
            insertLoginLog(username);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("登陆异常 {}", e.getMessage());
        }

        return token;
    }

    private void insertLoginLog(String username) {
        UmsAdmin umsAdmin = getAdminByUsername(username);
        if(umsAdmin == null) return;
        UmsAdminLoginLog adminLoginLog = new UmsAdminLoginLog();
        adminLoginLog.setAdminId(umsAdmin.getId());
        adminLoginLog.setCreateTime(new Date());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        adminLoginLog.setIp(RequestUtil.getRequestIp(requestAttributes.getRequest()));
        umsAdminLoginLogMapper.insert(adminLoginLog);
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtUtil.refreshToken(oldToken);
    }

    @Override
    public UmsAdmin getItem(Long id) {
        return umsAdminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsAdminExample example = new UmsAdminExample();
        if(!StrUtil.isEmpty(keyword)){
            example.createCriteria().andUsernameLike("%" + keyword + "%");
            example.createCriteria().andNickNameLike("%" + keyword + "%");
        }

        return umsAdminMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = umsAdminMapper.selectByPrimaryKey(id);
        if(rawAdmin.getPassword().equals(admin.getPassword())){
            admin.setPassword(null);
        }else{
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        int count = umsAdminMapper.updateByPrimaryKeySelective(admin);
        getCacheService().delAdmin(id);
        return count;
    }

    @Override
    public int delete(Long id) {
        getCacheService().delAdmin(id);
        getCacheService().delResourceList(id);
        return umsAdminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andAdminIdEqualTo(adminId);
        umsAdminRoleRelationMapper.deleteByExample(example);

        if(CollUtil.isNotEmpty(roleIds)){
            ArrayList<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation adminRoleRelation = new UmsAdminRoleRelation();
                adminRoleRelation.setAdminId(adminId);
                adminRoleRelation.setRoleId(roleId);
                list.add(adminRoleRelation);
            }
            umsAdminRoleRelationDao.insertList(list);
        }

        getCacheService().delResourceList(adminId);
        return roleIds.size();
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return umsAdminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public List<UmsResource> getResourceList(Long adminId) {
        List<UmsResource> resourceList = getCacheService().getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            return resourceList;
        }

        List<UmsResource> list = umsAdminRoleRelationDao.getResourceList(adminId);
        if(CollUtil.isNotEmpty(list)){
            getCacheService().setResourceList(adminId, list);
        }
        return list;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam updatePasswordParam) {
        if(StrUtil.isEmpty(updatePasswordParam.getUsername())
            || StrUtil.isEmpty(updatePasswordParam.getOldPassword())
            || StrUtil.isEmpty(updatePasswordParam.getNewPassword())){
            return -1; //参数不合法
        }

        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(updatePasswordParam.getUsername());
        List<UmsAdmin> umsAdminList = umsAdminMapper.selectByExample(example);
        if(CollUtil.isEmpty(umsAdminList)){
            return -2; //找不到用户
        }

        UmsAdmin umsAdmin = umsAdminList.get(0);
        if(!passwordEncoder.matches(updatePasswordParam.getOldPassword(), umsAdmin.getPassword())){
            return -3; //旧密码不正确
        }

        umsAdmin.setPassword(passwordEncoder.encode(updatePasswordParam.getNewPassword()));
        umsAdminMapper.updateByPrimaryKey(umsAdmin);
        getCacheService().delAdmin(umsAdmin.getId());
        return 0;
    }
}
