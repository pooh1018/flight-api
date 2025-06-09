
package com.example.flightapi.User.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author
 */
@Data
public class UserRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

//    private String email;

//    private String nickName;

    @Schema(description = "当前页码(从1开始)", 
            example = "1", 
            minimum = "1", 
            defaultValue = "1")
    private Integer pageNo = 1;

    @Schema(description = "起始记录索引(自动计算)", 
            example = "0", 
            minimum = "0")
    private Integer startIndex;

    @Schema(description = "每页记录数", 
            example = "10", 
            minimum = "1", 
            maximum = "100", 
            defaultValue = "10")
    private Integer pageSize = 10;

    @Schema(description = "排序字段", 
            example = "id", 
            allowableValues = {"id", "createTime", "updateTime"}, 
            defaultValue = "id")
    private String sortField = "id";

    @Schema(description = "排序方向", 
            example = "ASC", 
            allowableValues = {"ASC", "DESC"}, 
            defaultValue = "ASC")
    private String sortDirection = "ASC";
}
