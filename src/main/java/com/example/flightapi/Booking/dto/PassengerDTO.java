package com.example.flightapi.Booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Schema(description = "Passenger information data transfer object")
public class PassengerDTO {
    @Schema(description = "Unique passenger identifier",
            example = "507f1f77bcf86cd799439012")
    private String id;

    @Schema(description = "User ID", example = "123456")
    private int userId;

    @Schema(description = "Passenger User ID", example = "123456")
    private Integer passengerUserId;

    @Schema(description = "Passenger First name", example = "San", required = true)
    private String firstName;

    @Schema(description = "Passenger Last name", example = "Zhang", required = true)
    private String lastName;

    @Schema(description = "Passenger Email address", example = "passenger@example.com")
    private String email;

    @Schema(description = "Passenger Phone number", example = "13812345678")
    private String phone;
}
