package com.example.flightapi;

import com.example.flightapi.common.annotation.rest.AnonymousGetMapping;
import com.example.flightapi.common.utils.SpringBeanHolder;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Hidden
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableTransactionManagement
public class FlightApiApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FlightApiApplication.class);
        // 监控应用的PID，启动时可指定PID路径：--spring.pid.file=./FlightApiApplication.pid
        // 或者在 application.yml 添加文件路径，方便 kill，kill `cat ./FlightApiApplication.pid`
        springApplication.addListeners(new ApplicationPidFileWriter());
        ConfigurableApplicationContext context = springApplication.run(args);
//        SpringApplication.run(FlightApiApplication.class, args);

        // 获取环境配置
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8001");

        log.info("---------------------------------------------");

        // 获取服务器IP地址
        String serverAddress = getServerAddress();

        log.info("API URL: {}", "http://" + serverAddress + ":" + port);
        log.info("Swagger: {}", "http://" + serverAddress + ":" + port + "/swagger-ui.html");
        log.info("---------------------------------------------");
    }

    /**
     * 获取服务器IP地址
     * @return 服务器IP地址，如果获取失败则返回localhost
     */
    private static String getServerAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("Unable to obtain server IP address:{}", e.getMessage());
            return "localhost";
        }
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
