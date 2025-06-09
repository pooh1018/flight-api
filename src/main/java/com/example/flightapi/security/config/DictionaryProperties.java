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
@ConfigurationProperties(prefix = "dictionary")
public class DictionaryProperties {

    public static final String airportCityList = "airport_city_list";
}
