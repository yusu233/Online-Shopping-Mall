package com.pwc.security.component;

import cn.hutool.core.util.URLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 访问指定路径所需的权限管理类
 */
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private DynamicSecurityService dynamicSecurityService;
    private static Map<String, ConfigAttribute> attributeMap = null;

    @PostConstruct
    private void loadAttributeMap() {
        attributeMap = dynamicSecurityService.getAttributeMap();
    }

    public void clearAttributeMap() {
        attributeMap.clear();
        attributeMap = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (attributeMap == null) {
            this.loadAttributeMap();
        }

        List<ConfigAttribute> configAttributes = new ArrayList<>();
        String url = ((FilterInvocation) object).getRequestUrl();
        String path = URLUtil.getPath(url);
        AntPathMatcher pathMatcher = new AntPathMatcher();
        Iterator<String> iterator = attributeMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if(pathMatcher.match(key, path)){
                configAttributes.add(attributeMap.get(key));
            }
        }

        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
