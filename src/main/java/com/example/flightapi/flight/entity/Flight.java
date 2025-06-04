package com.example.flightapi.flight.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "flight")
public class Flight {
    @Id
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
