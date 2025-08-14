package com.example.flightapi.common.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedissonConfiguration {

    @Value("${spring.data.redis.host:127.0.0.1}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.database:2}")
    private int redisDatabase;

    @Value("${spring.data.redis.password:123456}")
    private String redisPassword;

    @Value("${spring.data.redis.timeout:5000}")
    private int timeout;

    @Value("${spring.data.redis.lettuce.pool.max-active:64}")
    private int connectionPoolSize;

    @Value("${spring.data.redis.lettuce.pool.min-idle:16}")
    private int connectionMinimumIdleSize;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setDatabase(redisDatabase)
                .setTimeout(timeout)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize);
        if(StrUtil.isNotBlank(redisPassword)){
            config.useSingleServer().setPassword(redisPassword);
        }
        return Redisson.create(config);
    }
}


