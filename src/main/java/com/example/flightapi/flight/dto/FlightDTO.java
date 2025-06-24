package com.example.flightapi.flight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Flight data transfer object containing flight schedule and availability information")
public class FlightDTO {
    @Schema(description = "Unique flight identifier",
            example = "507f1f77bcf86cd799439013")
    private String id;

    @Schema(description = "Flight number in airline format",
            example = "UA123")
    private String flightNumber;

    @Schema(description = "ID of the departure airport",
            example = "1001")
    private Integer departureAirportId;

    @Schema(description = "ID of the arrival airport",
            example = "2002")
    private Integer arrivalAirportId;

    @Schema(description = "Scheduled departure date and time",
            example = "2023-06-15T08:30:00")
    private LocalDateTime departureTime;

    @Schema(description = "Scheduled arrival date and time",
            example = "2023-06-15T11:45:00")
    private LocalDateTime arrivalTime;

    @Schema(description = "Base price for the flight",
            example = "299.99")
    private Double basePrice;

    @Schema(description = "Current flight status",
            example = "SCHEDULED",
            allowableValues = {"SCHEDULED", "DELAYED", "CANCELLED", "DEPARTED", "ARRIVED"})
    private String status;

    @Schema(description = "Number of available seats",
            example = "150")
    private Integer availableSeats;
}
