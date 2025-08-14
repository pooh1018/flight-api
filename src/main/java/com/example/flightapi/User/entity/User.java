package com.example.flightapi.User.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.*;
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

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "用户ID", example = "1000000000", required = true)
    @Field("user_id")
    @Min(value = 1000000000, message = "用户ID必须大于等于1000000000")
    private int userId;

    @Schema(description = "用户昵称", example = "飞行达人", maxLength = 50)
    @Field("nick_name")
    private String nickName;

    @Schema(description = "国家/地区代码", example = "CN", maxLength = 2)
    private String country;

    @Schema(description = "用户邮箱(登录账号)",
            example = "user@example.com",
            required = true,
            format = "email")
    private String email;

    @Schema(description = "名字", example = "三", maxLength = 50)
    @Field("first_name")
    private String firstName;

    @Schema(description = "姓氏", example = "张", maxLength = 50)
    @Field("last_name")
    private String lastName;

    @Schema(description = "密码(加密存储)",
            example = "$2a$10$N9qo8uLOickgx2ZMRZoMy...",
            accessMode = Schema.AccessMode.WRITE_ONLY,
            minLength = 60,
            maxLength = 100)
    private String password;

    @Schema(description = "手机号码", example = "13800138000", maxLength = 20)
    private String phone;

    @NotNull
    @Schema(description = "账号是否启用", example = "true", defaultValue = "true")
    private Boolean enabled;

    public String getUsername() {
        return email;
    }
}
