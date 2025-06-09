package com.example.flightapi.security.config;

import com.example.flightapi.security.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import com.example.flightapi.security.security.*;
import com.example.flightapi.security.service.OnlineUserService;
import com.example.flightapi.common.utils.AnonTagUtils;
import com.example.flightapi.common.utils.enums.RequestMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Map;
import java.util.Set;

/**
 * @author
 */
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig {

    // 这个类主要是获取库中的用户信息，交给security
    @Autowired
    private UserDetailService userDetailsService;

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ApplicationContext applicationContext;
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final CorsFilter corsFilter;
    private final AnonymousUrlsProperties anonymousUrlsProperties;

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 获取匿名标记
        Map<String, Set<String>> anonymousUrls = AnonTagUtils.getAnonymousUrl(applicationContext);
        return httpSecurity
                // 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 添加 CORS 过滤器
                .addFilter(corsFilter)
//                .addFilterBefore(corsFilter, ChannelProcessingFilter.class)
                // 处理请求
                .authorizeHttpRequests(authorize -> {
                    // 配置文件中定义的匿名访问URL
                    if (!anonymousUrlsProperties.getGet().isEmpty()) {
                        authorize.requestMatchers(
                                HttpMethod.GET,
                                anonymousUrlsProperties.getGet().toArray(new String[0])
                        ).permitAll();
                    }
                    if (!anonymousUrlsProperties.getPost().isEmpty()) {
                        authorize.requestMatchers(
                                HttpMethod.POST,
                                anonymousUrlsProperties.getPost().toArray(new String[0])
                        ).permitAll();
                    }
                    if (!anonymousUrlsProperties.getAll().isEmpty()) {
                        authorize.requestMatchers(
                                anonymousUrlsProperties.getAll().toArray(new String[0])
                        ).permitAll();
                    }
                    // 放行OPTIONS请求
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    // 自定义匿名访问所有url放行：允许匿名和带Token访问，细腻化到每个 Request 类型
                    // GET
                    authorize.requestMatchers(
                            HttpMethod.GET,
                            anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0])
                    ).permitAll();
                    // POST
                    authorize.requestMatchers(
                            HttpMethod.POST,
                            anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0])
                    ).permitAll();
                    // PUT
                    authorize.requestMatchers(
                            HttpMethod.PUT,
                            anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0])
                    ).permitAll();
                    // PATCH
                    authorize.requestMatchers(
                            HttpMethod.PATCH,
                            anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0])
                    ).permitAll();
                    // DELETE
                    authorize.requestMatchers(
                            HttpMethod.DELETE,
                            anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0])
                    ).permitAll();
                    // 所有类型的接口都放行
                    authorize.requestMatchers(
                            anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0])
                    ).permitAll();
                    // Swagger相关资源放行
                    authorize.requestMatchers("/swagger-ui/**").permitAll();
                    authorize.requestMatchers("/swagger-resources/**").permitAll();
                    authorize.requestMatchers("/v3/api-docs/**").permitAll();
                    // 其他的都需要认证
                    authorize.anyRequest().authenticated();
                })
                // 错误处理
                .exceptionHandling(m -> {
                    m.authenticationEntryPoint(authenticationErrorHandler);
                    m.accessDeniedHandler(jwtAccessDeniedHandler);
                })
                // 如果使用token这个配置是必须的
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                // 这个不配置也会生效
                .authenticationProvider(authenticationProvider())
                .httpBasic(Customizer.withDefaults()).formLogin(Customizer.withDefaults())
                .build();
    }

    /**
     * 这个是如果使用token方式需要在这个类中先处理token（根据token判断是否去认证）
     */
    @Bean
    public TokenFilter authenticationJwtTokenFilter() {
        return new TokenFilter(tokenProvider, properties, onlineUserService);
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
