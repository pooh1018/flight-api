package com.example.flightapi.User.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改密码的 Vo 类
 * @author
 */
@Data
public class UserPassRequestDTO {

    @ApiModelProperty(value = "旧密码")
    private String oldPass;

    @ApiModelProperty(value = "新密码")
    private String newPass;
}
