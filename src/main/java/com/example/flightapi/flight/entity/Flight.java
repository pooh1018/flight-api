package com.example.flightapi.flight.entity;

import com.example.flightapi.cabin.entity.CabinClass;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "flight")
@Schema(description = "Flight information including schedule and route details")
public class Flight {

    @Id
    @Schema(description = "Unique identifier of the flight", example = "fl123")
    private String id;

    @Schema(description = "Flight number", example = "AA1234")
    @Field("flight_number")
    private String flightNumber;

    @Schema(description = "Airport id where the flight departs from", example = "1001")
    @Field("departure_airport_id")
    private int departureAirportId;

    @Schema(description = "Airport id where the flight arrives at", example = "1002")
    @Field("destination_airport_id")
    private int destinationAirportId;

    @Schema(description = "Scheduled departure time", example = "2023-12-25T08:30:00")
    @Field("departure_time")
    private LocalDateTime departureTime;

    @Schema(description = "Scheduled arrival time", example = "2023-12-25T11:45:00")
    @Field("arrival_time")
    private LocalDateTime arrivalTime;

    @Schema(description = "Status of the flight (e.g., SCHEDULED, DELAYED, CANCELLED)", example = "SCHEDULED")
    private String status;

    @Field("price")
    private Double price;

    @Schema(description = "stops times")
    @Field("stops")
    private int stops;

    @Schema(description = "List of cabin classes available on this flight")
    @Field("cabin_classes")
    private List<CabinClass> cabinClasses;
}
