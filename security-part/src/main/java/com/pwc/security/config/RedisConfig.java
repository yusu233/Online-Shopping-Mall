package com.pwc.security.config;

import com.pwc.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

//TODO
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}