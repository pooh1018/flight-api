package com.example.flightapi.flight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Schema(description = "Flight search criteria")
public class SearchFlightRequestDTO {

    @NotNull
    @Schema(description = "Departure airport ID", required = true)
    private Integer departureAirportId;

    @NotNull
    @Schema(description = "Destination airport ID", required = true)
    private Integer destinationAirportId;

    @Schema(description = "Start date and time for search range (ISO format)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @Schema(description = "End date and time for search range (ISO format)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
}
