package com.pwc.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@MapperScan({"com.pwc.mapper", "com.pwc.search.dao"})
public class MybatisConfig {
}
