package com.example.flightapi.User.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改密码的 Vo 类
 * @author
 */
@Data
public class UserPassRequestDTO {

    @ApiModelProperty(value = "email")
    private String email;

    @ApiModelProperty(value = "old password")
    private String oldPass;

    @ApiModelProperty(value = "new password")
    private String newPass;

    @ApiModelProperty(value = "confirm password")
    private String confirmPass;
}
