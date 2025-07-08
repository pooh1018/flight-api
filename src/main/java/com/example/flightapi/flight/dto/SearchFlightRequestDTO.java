package com.example.flightapi.flight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Data
@Schema(description = "Flight search criteria")
public class SearchFlightRequestDTO {

    @NotNull
    @Schema(description = "Departure airport ID", required = true)
    private Integer departureAirportId;

    @NotNull
    @Schema(description = "Destination airport ID", required = true)
    private Integer destinationAirportId;

    @Schema(description = "Start date and time for search range (ISO format with timezone, e.g. 2025-07-08T00:00:00.000Z)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime startDate;

    @Schema(description = "End date and time for search range (ISO format with timezone, e.g. 2025-07-08T23:59:59.999Z)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime endDate;
    
    // 添加getter方法，将OffsetDateTime转换为LocalDateTime
    public LocalDateTime getStartDateAsLocalDateTime() {
        return startDate != null ? startDate.toLocalDateTime() : null;
    }
    
    public LocalDateTime getEndDateAsLocalDateTime() {
        return endDate != null ? endDate.toLocalDateTime() : null;
    }
}
