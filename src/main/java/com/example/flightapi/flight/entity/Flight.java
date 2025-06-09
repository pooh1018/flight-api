package com.example.flightapi.flight.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Data
@Document(collection = "flight")
public class Flight {
    @Id
    private String id;

    @Field("flight_number")
    private String flightNumber;

    @Field("departure_airport_id")
    private Integer departureAirportId;

    @Field("arrival_airport_id")
    private Integer arrivalAirportId;

    @Field("departure_time")
    private LocalDateTime departureTime;

    @Field("arrival_time")
    private LocalDateTime arrivalTime;

    @Field("base_price")
    private Double basePrice;

    @Field("status")
    private String status;

    @Field("available_seats")
    private Integer availableSeats;
}
