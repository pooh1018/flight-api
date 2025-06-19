package com.example.flightapi.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("#{'${cors.allowed-origin-patterns:http://8.137.95.47:*,http://http://47.109.24.42/:*,http://localhost:*,*://*:80}'.split(',')}")
    private List<String> allowedOriginPatterns;

    @Value("#{'${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}'.split(',')}")
    private List<String> allowedMethods;

    @Value("#{'${cors.allowed-headers:*}'.split(',')}")
    private List<String> allowedHeaders;

    @Value("${cors.max-age:3600}")
    private Long maxAge;

    @Value("${cors.allow-credentials:true}")
    private Boolean allowCredentials;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOriginPatterns.toArray(new String[0]))  // 使用配置文件中的模式列表
                .allowedMethods(allowedMethods.toArray(new String[0]))  // 使用配置文件中的方法列表
                .allowedHeaders(allowedHeaders.toArray(new String[0]))  // 使用配置文件中的请求头列表
                .allowCredentials(allowCredentials)  // 使用配置文件中的认证信息设置
                .maxAge(maxAge);  // 使用配置文件中的最大缓存时间
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 设置允许的来源模式
        config.setAllowedOriginPatterns(allowedOriginPatterns);

        // 设置允许的请求头
        config.setAllowedHeaders(allowedHeaders);

        // 设置允许的方法
        config.setAllowedMethods(allowedMethods);

        // 允许携带认证信息
        config.setAllowCredentials(allowCredentials);

        // 预检请求的有效期，单位为秒
        config.setMaxAge(maxAge);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
