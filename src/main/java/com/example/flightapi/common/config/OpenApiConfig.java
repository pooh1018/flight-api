package com.example.flightapi.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("航班管理系统 API")
                        .description("航班预订和管理系统的RESTful API文档")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Flight API Team")
                                .email("support@flightapi.com")
                                .url("https://flightapi.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server().url("http://47.109.24.42:8001/api").description("默认服务器")
                ));
    }
}
