package com.example.flightapi.User.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.flightapi.User.service.UserService;
import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @ApiOperation("查询所有用户")
    @GetMapping(value = "/getAll")
    @PreAuthorize("@el.check('user:list')")
    public ApiResult getAllUser() {
        return ApiResult.success(userService.findAll());
    }

    @ApiOperation("查询所有用户（分页）")
    @GetMapping
    public ApiResult getAllUserByPage(@RequestBody UserRequestDTO user) {
        Page<Object> page = new Page<>(user.getPageNo() - 1, user.getPageSize());
        return ApiResult.success(userService.findAll(user));
    }

    @ApiOperation("通过id查询用户")
    @GetMapping(value = "/findById")
    @PreAuthorize("@el.check('user:find')")
    public ApiResult findById(@RequestParam String id) {
        return ApiResult.success(userService.findById(id));
    }

    @ApiOperation("通过email查询用户")
    @GetMapping(value = "/findByEmail")
    public ApiResult findByEmail(@RequestParam String email) {
        return ApiResult.success(userService.findByEmail(email));
    }

    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ApiResult createUser(@Validated @RequestBody User resources) {
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        userService.create(resources);
        return ApiResult.success();
    }

    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ApiResult updateUser(@RequestBody User resources) throws Exception {
        userService.update(resources);
        return ApiResult.success();
    }

    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ApiResult centerUser(@RequestBody User resources) {
        if (!resources.getId().equals(SecurityUtils.getCurrentUserId())) {
            return ApiResult.fail(messageUtils.getMessage("user.modifyInfo.error"));
        }
        userService.updateCenter(resources);
        return ApiResult.success();
    }

    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ApiResult deleteUser(@RequestBody Set<String> ids) {
        userService.delete(ids);
        return ApiResult.success(HttpStatus.OK);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ApiResult updateUserPass(@RequestBody UserPassRequestDTO passVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
        User user = userService.findByEmail(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            return ApiResult.fail(messageUtils.getMessage("user.oldpassword.error"));
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            return ApiResult.fail(messageUtils.getMessage("user.newoldpassword.error"));
        }
        userService.updatePass(user.getEmail(), passwordEncoder.encode(newPass));
        return ApiResult.success();
    }

    @ApiOperation("重置密码")
    @PutMapping(value = "/resetPwd")
    public ApiResult resetPwd(@RequestBody Set<String> ids) {
        String pwd = passwordEncoder.encode("123456");
        userService.resetPwd(ids, pwd);
        return ApiResult.success();
    }
}
