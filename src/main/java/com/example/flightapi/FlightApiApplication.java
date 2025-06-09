package com.example.flightapi;

import com.example.flightapi.common.annotation.rest.AnonymousGetMapping;
import com.example.flightapi.common.utils.SpringBeanHolder;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Hidden
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableTransactionManagement
public class FlightApiApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FlightApiApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
//        SpringApplication.run(FlightApiApplication.class, args);
        log.info("---------------------------------------------");
        log.info("Local: {}", "http://localhost:8001");
        log.info("Swagger: {}", "http://localhost:8001/swagger-ui.html");
        log.info("---------------------------------------------");
    }

    @Bean
    public SpringBeanHolder springContextHolder() {
        return new SpringBeanHolder();
    }

    /**
     * 访问首页提示
     * @return /
     */
    @AnonymousGetMapping("/")
    public String index() {
        return "Backend service started successfully";
    }
}
