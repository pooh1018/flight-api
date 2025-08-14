package com.example.flightapi.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 匿名访问接口配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "security.anonymous-urls")
public class AnonymousUrlsProperties {

    /**
     * GET请求可匿名访问的接口列表
     */
    private List<String> get = new ArrayList<>();

    /**
     * POST请求可匿名访问的接口列表
     */
    private List<String> post = new ArrayList<>();

    /**
     * 所有方法都可匿名访问的接口列表
     */
    private List<String> all = new ArrayList<>();
}
