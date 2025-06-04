package com.example.flightapi.User.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
@TableName("user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "user id")
    @Field("user_id")
    private int userId;

    @ApiModelProperty(value = "nick name")
    @Field("nick_name")
    private String nickName;

    @ApiModelProperty(value = "country")
    private String country;

    @ApiModelProperty(value = "email")
    private String email;

    @ApiModelProperty(value = "first name")
    @Field("first_name")
    private String firstName;

    @ApiModelProperty(value = "last name")
    @Field("last_name")
    private String lastName;

    @ApiModelProperty(value = "password")
    private String password;

    @ApiModelProperty(value = "phone")
    private String phone;

    @NotNull
    @ApiModelProperty(value = "enabled")
    private Boolean enabled;

    public String getUsername() {
        return email;
    }
}
