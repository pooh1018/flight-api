package com.example.flightapi.Booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@Document(collection = "passenger")
@Schema(description = "Passenger information")
public class Passenger {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "Passenger ID", example = "63f7e8d9e4b0a1a9c8d9e4b1")
    private String id;

    @Field("user_id")
    @Schema(description = "User ID", example = "123456")
    private int userId;

    @Field("passenger_user_id")
    @Schema(description = "Passenger User ID", example = "123456")
    private Integer passengerUserId;

    @Field("first_name")
    @Schema(description = "First name", example = "San", required = true)
    private String firstName;

    @Field("last_name")
    @Schema(description = "Last name", example = "Zhang", required = true)
    private String lastName;

    @Field("email")
    @Schema(description = "Email address", example = "passenger@example.com")
    private String email;

    @Field("phone")
    @Schema(description = "Phone number", example = "13812345678")
    private String phone;
}
