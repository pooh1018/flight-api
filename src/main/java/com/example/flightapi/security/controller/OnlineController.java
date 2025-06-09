package com.example.flightapi.security.controller;

import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "System: Online User Management")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    @Operation(summary = "Query online users", description = "Get list of currently logged in users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Query successful"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    @PreAuthorize("@el.check()")
    public ApiResult queryOnlineUser(String username, Pageable pageable){
        return ApiResult.success(onlineUserService.getAll(username, pageable));
    }

    @Operation(summary = "Force logout users", description = "Log out selected users by their tokens")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
