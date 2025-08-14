package com.example.flightapi.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件读取
 *
 * @author
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "login")
public class LoginProperties {

    /**
     * 账号单用户 登录
     */
    private boolean singleLogin = false;

    public static final String cacheKey = "user_login_cache:";
}
