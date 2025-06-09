package com.example.flightapi.Booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@Document(collection = "passenger")
public class Passenger {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Field("booking_id")
    private String bookingId;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("email")
    private String email;

    // 不在MongoDB Schema中定义，但对业务有用的可选字段
    @Field("seat_number")
    private String seatNumber;

    @DBRef(lazy = true)
    private Booking booking;
}
