package com.example.flightapi.airport.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "airport")
public class Airport {
    @Id
    private String id;

    private Integer airportId;
    private String airportCode;
    private String airportName;
    private String city;
    private String country;
}
