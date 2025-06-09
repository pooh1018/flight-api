package com.example.flightapi.Booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Passenger information data transfer object")
public class PassengerDTO {
    @Schema(description = "Unique passenger identifier",
            example = "507f1f77bcf86cd799439012")
    private String id;
    
    @Schema(description = "ID of the booking this passenger belongs to",
            example = "507f1f77bcf86cd799439011")
    private String bookingId;
    
    @Schema(description = "Passenger's first name",
            example = "John")
    private String firstName;
    
    @Schema(description = "Passenger's last name",
            example = "Smith")
    private String lastName;
    
    @Schema(description = "Assigned seat number",
            example = "12A")
    private String seatNumber;
}
