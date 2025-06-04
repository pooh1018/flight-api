package com.example.flightapi.security.controller;

import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.example.flightapi.security.service.OnlineUserService;
import com.example.flightapi.security.dto.OnlineUserDto;
import com.example.flightapi.common.utils.EncryptUtils;
import com.example.flightapi.common.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    @ApiOperation("查询在线用户")
    @GetMapping
    @PreAuthorize("@el.check()")
    public ApiResult queryOnlineUser(String username, Pageable pageable){
        return ApiResult.success(onlineUserService.getAll(username, pageable));
    }

    @ApiOperation("踢出用户")
    @DeleteMapping
    @PreAuthorize("@el.check()")
    public ApiResult deleteOnlineUser(@RequestBody Set<String> keys) throws Exception {
        for (String token : keys) {
            // 解密Key
            token = EncryptUtils.desDecrypt(token);
            onlineUserService.logout(token);
        }
        return ApiResult.success(HttpStatus.OK);
    }
}
