package com.example.flightapi.User.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.flightapi.User.service.UserService;
import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.flightapi.common.config.properties.RsaProperties;
import com.example.flightapi.User.entity.User;
import com.example.flightapi.User.dto.UserPassRequestDTO;
import com.example.flightapi.User.dto.UserRequestDTO;
import com.example.flightapi.common.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author
 */
@Tag(name = "User Management", description = "System user management APIs including CRUD operations, password modification etc.")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Operation(
        summary = "查询所有用户",
        description = "Get complete list of all users in the system, requires 'user:list' permission",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
        }
    )
    @GetMapping(value = "/getAll")
    @PreAuthorize("@el.check('user:list')")
    public ApiResult getAllUser() {
        return ApiResult.success(userService.findAll());
    }

    @Operation(
        summary = "Query users with pagination",
        description = "Get paginated user list with filtering support",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated user list"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
        }
    )
    @GetMapping
    public ApiResult getAllUserByPage(@RequestBody UserRequestDTO user) {
        Page<Object> page = new Page<>(user.getPageNo() - 1, user.getPageSize());
        return ApiResult.success(userService.findAll(user));
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieve detailed user information by user ID, requires 'user:find' permission",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user information"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    @GetMapping(value = "/findById")
    @PreAuthorize("@el.check('user:find')")
    public ApiResult findById(@RequestParam String id) {
        return ApiResult.success(userService.findById(id));
    }

    @Operation(
        summary = "Find user by email",
        description = "Retrieve user information by exact email match",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user information"),
            @ApiResponse(responseCode = "400", description = "Invalid email format"),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    @GetMapping(value = "/findByEmail")
    public ApiResult findByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if(user == null) {
            return ApiResult.failMessage(messageUtils.getMessage("user.not.found"));
        } else {
            return ApiResult.success(user);
        }
    }

    @Operation(
        summary = "Create user",
        description = "Create new user account with default password '123456', requires 'user:add' permission",
        responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Request parameter validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
        }
    )
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ApiResult createUser(@Validated @RequestBody User resources) {
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        userService.create(resources);
        return ApiResult.success();
    }

    @Operation(
        summary = "Update user information",
        description = "Update detailed user information, requires 'user:edit' permission",
        responses = {
            @ApiResponse(responseCode = "200", description = "User information updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ApiResult updateUser(@RequestBody User resources) throws Exception {
        userService.update(resources);
        return ApiResult.success();
    }

    @Operation(
        summary = "Update personal profile",
        description = "Update basic information of currently logged-in user (excluding password changes)",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Can only modify own information")
        }
    )
    @PutMapping(value = "center")
    public ApiResult centerUser(@RequestBody User resources) {
        if (!resources.getId().equals(SecurityUtils.getCurrentUserId())) {
            return ApiResult.failMessage(messageUtils.getMessage("user.modifyInfo.error"));
        }
        userService.updateCenter(resources);
        return ApiResult.success();
    }

    @Operation(
        summary = "Delete users",
        description = "Batch delete users by ID collection, requires 'user:del' permission",
        responses = {
            @ApiResponse(responseCode = "200", description = "Users deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID collection format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Some users not found")
        }
    )
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ApiResult deleteUser(@RequestBody Set<String> ids) {
        userService.delete(ids);
        return ApiResult.success(HttpStatus.OK);
    }

    @Operation(
        summary = "Update user password",
        description = "User updates their own login password, requires both old and new passwords",
        responses = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password format or same old/new password"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Password encryption/decryption error")
        }
    )
    @PostMapping(value = "/updatePass")
    public ApiResult updateUserPass(@RequestBody UserPassRequestDTO passVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
        User user = userService.findByEmail(SecurityUtils.getCurrentUsername());
//        User user = userService.getLoginData(passVo.getEmail());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            return ApiResult.failMessage(messageUtils.getMessage("user.oldpassword.error"));
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            return ApiResult.failMessage(messageUtils.getMessage("user.newoldpassword.error"));
        }
        userService.updatePass(user.getEmail(), passwordEncoder.encode(newPass));
        return ApiResult.success();
    }

    @Operation(
        summary = "Reset user passwords",
        description = "Administrator batch resets user passwords to default '123456'",
        responses = {
            @ApiResponse(responseCode = "200", description = "Passwords reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID collection format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Some users not found")
        }
    )
    @PutMapping(value = "/resetPwd")
    public ApiResult resetPwd(@RequestBody Set<String> ids) {
        String pwd = passwordEncoder.encode("123456");
        userService.resetPwd(ids, pwd);
        return ApiResult.success();
    }

    @Operation(
        summary = "Reset password via email",
        description = "User resets password through email verification, requires providing email and new password",
        responses = {
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Password mismatch or invalid format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Email not found"),
            @ApiResponse(responseCode = "500", description = "Password encryption/decryption error")
        }
    )
    @PostMapping(value = "/resetPwdByEmail")
    public ApiResult resetPwdByEmail(@RequestBody UserPassRequestDTO passVo) throws Exception {

        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
        String confirmPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getConfirmPass());
        if (!newPass.equals(confirmPass)) {
            return ApiResult.failMessage(messageUtils.getMessage("user.newconfirmpassword.error"));
        }
        User user = userService.findByEmail(passVo.getEmail());
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            return ApiResult.failMessage(messageUtils.getMessage("user.newoldpassword.error"));
        }
        userService.updatePass(passVo.getEmail(), passwordEncoder.encode(newPass));;
        return ApiResult.success();
    }

}
