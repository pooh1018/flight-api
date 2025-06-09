package com.example.flightapi.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author
 */
@Getter
@Setter
public class AuthUserDto {

    @NotBlank
    @Schema(description = "邮箱", example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "密码", example = "password123")
    private String password;

//    @Schema(description = "验证码", example = "123456")
//    private String code;

//    @Schema(description = "验证码的key", example = "uuid-12345")
//    private String uuid = "";
}
