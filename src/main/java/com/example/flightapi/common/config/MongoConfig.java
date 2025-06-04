package com.example.flightapi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableMongoAuditing
public class MongoConfig {

    // 关系型数据库事务管理器
//    @Bean(name = "jdbcTransactionManager")
//    public DataSourceTransactionManager jdbcTransactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

    // MongoDB 事务管理器
    @Bean(name = "mongoTransactionManager")
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

}
