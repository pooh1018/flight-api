package com.example.flightapi.flight.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FlightDTO {
    private String id;
    private String flightNumber;
    private Integer departureAirportId;
    private Integer arrivalAirportId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double basePrice;
    private String status;
    private Integer availableSeats;
}
