package com.example.flightapi.Booking.dto;

import com.example.flightapi.Booking.entity.Passenger;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Flight booking data transfer object containing reservation details")
public class BookingDTO {
    @Schema(description = "Unique booking identifier",
            example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "ID of the user who made the booking",
            example = "12345")
    private int userId;

    @Schema(description = "ID of the flight being booked",
            example = "FL-2023-1001")
    private String flightId;

    @Schema(description = "List of passengers with seat assignments")
    private List<PassengerInfo> passengers;

    @Data
    @Schema(description = "Passenger information with seat assignment")
    public static class PassengerInfo {
        @Field(targetType = FieldType.OBJECT_ID)
        @Schema(description = "Passenger ID", example = "63f7e8d9e4b0a1a9c8d9e4b1")
        private String passengerId;

        @Schema(description = "Seat number", example = "10A")
        private String seatNumber;
    }

    @Schema(description = "Total price of the booking",
            example = "599.99")
    private BigDecimal totalPrice;

    @Schema(description = "Current status of the booking",
            example = "CONFIRMED",
            allowableValues = {"CONFIRMED", "PENDING", "CANCELLED"})
    private String status;

    @Schema(description = "Date and time when booking was made",
            example = "2023-05-15T14:30:00")
    private LocalDateTime bookingTime;

    @Schema(description = "Payment method used",
            example = "CREDIT_CARD",
            allowableValues = {"CREDIT_CARD", "PAYPAL", "BANK_TRANSFER"})
    private String paymentMethod;

    @Schema(description = "Contact email for booking notifications",
            example = "customer@example.com")
    private String contactEmail;

    @Schema(description = "Contact phone number",
            example = "+1-555-123-4567")
    private String contactPhone;
}

