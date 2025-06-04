package com.example.flightapi.security.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

//    @ApiModelProperty(value = "验证码")
//    private String code;

//    @ApiModelProperty(value = "验证码的key")
//    private String uuid = "";
}
