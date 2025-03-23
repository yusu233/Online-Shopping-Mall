package com.pwc.portal.config;

import com.pwc.portal.service.UmsMemberService;
import com.pwc.portal.service.impl.UmsMemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

@Configuration
public class PortalSecurityConfig {
    @Resource
    private UmsMemberService memberService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> memberService.loadUserByUsername(username);
    }
}
