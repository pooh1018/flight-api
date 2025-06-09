package com.example.flightapi.Booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "booking")
public class Booking {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Field("user_id")
    private int userId;

    @Field("flight_id")
    private String flightId;

    @Field("reference")
    private String reference;

    @DBRef(lazy = true)
    @Field("passengers")
    private List<Passenger> passengers;

    @Field("total_price")
    private BigDecimal totalPrice;

    @Field("status")
    private String status;

    @Field("booking_time")
    private LocalDateTime bookingTime;

    // 以下字段不在MongoDB Schema中定义，但可能对业务有用，标记为可选字段
    @Field("payment_method")
    private String paymentMethod;

    @Field("contact_email")
    private String contactEmail;

    @Field("contact_phone")
    private String contactPhone;
}
