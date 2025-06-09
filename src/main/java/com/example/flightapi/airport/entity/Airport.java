package com.example.flightapi.airport.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "airport")
@Schema(description = "Airport persistence entity representing airport data in MongoDB")
public class Airport {
    @Id
    @Schema(description = "MongoDB unique identifier", 
            example = "507f1f77bcf86cd799439011")
    private String id;

    @Field("airport_id")
    @Schema(description = "Internal airport ID number", 
            example = "1001")
    private Integer airportId;
    
    @Field("airport_code") 
    @Schema(description = "IATA airport code", 
            example = "JFK")
    private String airportCode;
    
    @Field("airport_name")
    @Schema(description = "Full name of the airport", 
            example = "John F. Kennedy International Airport")
    private String airportName;
    
    @Field("city")
    @Schema(description = "City where airport is located", 
            example = "New York")
    private String city;
    
    @Field("country")
    @Schema(description = "Country where airport is located", 
            example = "United States")
    private String country;
}
