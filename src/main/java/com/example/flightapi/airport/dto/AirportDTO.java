package com.example.flightapi.airport.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Airport data transfer object containing airport information")
public class AirportDTO {
    @Schema(description = "Unique identifier of the airport", 
            example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Internal airport ID number", 
            example = "1001")
    private Integer airportId;
    
    @Schema(description = "IATA airport code", 
            example = "JFK")
    private String airportCode;
    
    @Schema(description = "Full name of the airport", 
            example = "John F. Kennedy International Airport")
    private String airportName;
    
    @Schema(description = "City where airport is located", 
            example = "New York")
    private String city;
    
    @Schema(description = "Country where airport is located", 
            example = "United States")
    private String country;
}
