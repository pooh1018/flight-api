
package com.example.flightapi.User.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author
 */
@Data
public class UserRequestDTO implements Serializable {

//    private String email;

//    private String nickName;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "开始位置", example = "1")
    private Integer startIndex;

    @ApiModelProperty(value = "每页数据量", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "sort field", example = "id")
    private String sortField = "id";

    @ApiModelProperty(value = "sort field", example = "ASC")
    private String sortDirection = "ASC";
}
