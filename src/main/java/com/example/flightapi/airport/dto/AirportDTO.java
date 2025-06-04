package com.example.flightapi.airport.dto;

import lombok.Data;

@Data
public class AirportDTO {
    private String id;
    private Integer airportId;
    private String airportCode;
    private String airportName;
    private String city;
    private String country;
}
