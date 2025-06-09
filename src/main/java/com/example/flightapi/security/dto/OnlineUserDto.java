package com.example.flightapi.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 在线用户
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserDto {

    @Schema(description = "Token编号", example = "abc123xyz")
    private String uid;

    @Schema(description = "用户名", example = "admin")
    private String userName;

    @Schema(description = "昵称", example = "系统管理员")
    private String nickName;

    @Schema(description = "浏览器", example = "Chrome 115")
    private String browser;

    @Schema(description = "IP", example = "192.168.1.100")
    private String ip;

    @Schema(description = "地址", example = "北京市海淀区")
    private String address;

    @Schema(description = "token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String key;

    @Schema(description = "登录时间", example = "2023-07-20T10:15:30Z")
    private Date loginTime;
}
