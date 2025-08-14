package com.example.flightapi.common.config;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    
    // 配置自定义转换器
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new ZonedDateTimeReadConverter());
        converters.add(new ZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }
    
    // ZonedDateTime 读取转换器
    static class ZonedDateTimeReadConverter implements Converter<String, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(String source) {
            return ZonedDateTime.parse(source, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        }
    }
    
    // ZonedDateTime 写入转换器
    static class ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, String> {
        @Override
        public String convert(ZonedDateTime source) {
            return source.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        }
    }
}
