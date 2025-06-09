package com.example.flightapi.security.controller;

import com.example.flightapi.User.entity.User;
import com.example.flightapi.User.service.UserService;
import com.example.flightapi.common.config.properties.RsaProperties;
import com.example.flightapi.common.exception.handler.ApiResult;
import com.example.flightapi.common.utils.MessageUtils;
import com.example.flightapi.common.utils.RsaUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.flightapi.common.annotation.rest.AnonymousDeleteMapping;
import com.example.flightapi.common.annotation.rest.AnonymousPostMapping;
import com.example.flightapi.security.config.LoginProperties;
import com.example.flightapi.security.config.SecurityProperties;
import com.example.flightapi.security.security.TokenProvider;
import com.example.flightapi.security.service.OnlineUserService;
import com.example.flightapi.security.service.UserDetailService;
import com.example.flightapi.security.dto.AuthUserDto;
import com.example.flightapi.security.dto.JwtUserDto;
import com.example.flightapi.common.utils.RedisUtils;
import com.example.flightapi.common.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "System: Authentication APIs")
@RequestMapping("/auth")
public class AuthController {

    private final MessageUtils messageUtils;
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final LoginProperties loginProperties;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailService userDetailsService;
    private final UserService userService;

    @Operation(summary = "User login", description = "Authenticate user credentials and return JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "400", description = "Invalid username or password"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @AnonymousPostMapping(value = "/login")
    public ApiResult login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 获取用户信息
        JwtUserDto jwtUser = userDetailsService.loadUserByUsername(authUser.getEmail());
        // 验证用户密码
        if (!passwordEncoder.matches(password, jwtUser.getPassword())) {
            return ApiResult.failMessage(messageUtils.getMessage("user.password.error"));
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(jwtUser);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUser);
        }};
        if (loginProperties.isSingleLogin()) {
            // 踢掉之前已经登录的token
            onlineUserService.kickOutForUsername(authUser.getEmail());
        }
        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);
        // 返回登录信息
        return ApiResult.success(authInfo);
    }

    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "Invalid user information"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/register")
    public ApiResult register(@Validated @RequestBody User resources) throws Exception {
        String passWord = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, resources.getPassword());
        resources.setPassword(passwordEncoder.encode(passWord));
        userService.create(resources);
        return ApiResult.success();
    }

    @Operation(summary = "Get user info", description = "Retrieve authenticated user's information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User info retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/info")
    public ApiResult getUserInfo() {
        JwtUserDto jwtUser = (JwtUserDto) SecurityUtils.getCurrentUser();
        return ApiResult.success(jwtUser);
    }

    @Operation(summary = "User logout", description = "Invalidate user's authentication token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @AnonymousDeleteMapping(value = "/logout")
    public ApiResult logout(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        onlineUserService.logout(token);
        return ApiResult.success(HttpStatus.OK);
    }
}
