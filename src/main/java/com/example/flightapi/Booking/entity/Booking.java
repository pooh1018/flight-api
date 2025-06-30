package com.example.flightapi.Booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.Decimal128;

@Data
@Document(collection = "booking")
@Schema(description = "Flight booking information")
public class Booking {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "Booking ID", example = "63f7e8d9e4b0a1a9c8d9e4b0")
    private String id;

    @Field("user_id")
    @Schema(description = "User ID", example = "123456")
    private int userId;

    @Field(value = "flight_id", targetType = FieldType.OBJECT_ID)
    @Schema(description = "Flight ID", example = "FL123")
    private String flightId;

    @Field("reference")
    @Schema(description = "Booking reference number", example = "BOOK-REF-2023")
    private String reference;

    @Field("passengers")
    @Schema(description = "List of passengers with seat assignments")
    private List<PassengerInfo> passengers;

    @Data
    @Schema(description = "Passenger information with seat assignment")
    public static class PassengerInfo {
        @Field(value = "passenger_id", targetType = FieldType.OBJECT_ID)
        @Schema(description = "Passenger ID", example = "63f7e8d9e4b0a1a9c8d9e4b1")
        private String passengerId;

        @Field("seat_number")
        @Schema(description = "Seat number", example = "10A")
        private String seatNumber;
    }

    @Field(value = "total_price", targetType = FieldType.DECIMAL128)
    @Schema(description = "Total price", example = "999.99")
    private BigDecimal totalPrice;

    @Field("status")
    @Schema(description = "Booking status",
           example = "CONFIRMED",
           allowableValues = {"CONFIRMED", "PENDING", "CANCELLED", "COMPLETED"})
    private String status;

    @Field("booking_time")
    @Schema(description = "Booking timestamp", example = "2023-02-20T15:30:00")
    private LocalDateTime bookingTime;

    @Field("payment_method")
    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;

    @Field("cabin_class_type")
    @Schema(description = "Type of cabin class (ECONOMY, Premium Economy, BUSINESS, FIRST)", example = "ECONOMY")
    private int cabinClassType;

    @Field("contact_email")
    @Schema(description = "contact email",
            example = "user@example.com",
            required = true,
            format = "email")
    private String contactEmail;

    @Field("contact_phone")
    @Schema(description = "contact phone", example = "13800138000", maxLength = 20)
    private String contactPhone;
}
