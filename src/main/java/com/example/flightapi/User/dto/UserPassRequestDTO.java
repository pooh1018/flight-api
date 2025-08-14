package com.example.flightapi.User.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改密码的 Vo 类
 * @author
 */
@Data
public class UserPassRequestDTO {

    @Schema(description = "用户邮箱", required = true, example = "user@example.com")
    private String email;

    @Schema(description = "旧密码", required = true, minLength = 6, maxLength = 20)
    private String oldPass;

    @Schema(description = "新密码", required = true, minLength = 6, maxLength = 20)
    private String newPass;

    @Schema(description = "确认密码(必须与新密码一致)", required = true, minLength = 6, maxLength = 20)
    private String confirmPass;
}
