package com.example.flightapi.cabin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for cabin class information")
public class CabinClassDTO {
    @Schema(description = "Unique identifier of the cabin class",
            example = "cc123")
    private String id;

    @Schema(description = "ID of the flight this cabin class belongs to",
            example = "fl456",
            required = true)
    private String flightId;

    @Schema(description = "Numeric identifier for the class type",
            example = "1",
            required = true)
    private Integer classType;

    @Schema(description = "Name of the cabin class",
            example = "Business",
            required = true)
    private String name;

    @Schema(description = "Number of seats currently available in this cabin class",
            example = "30",
            required = true,
            minimum = "0")
    private Integer availableSeats;

    @Schema(description = "Total number of seats in this cabin class",
            example = "50",
            required = true,
            minimum = "0")
    private Integer totalSeats;

    @Schema(description = "Multiplier used to calculate the final price",
            example = "1.5",
            required = true,
            minimum = "0")
    private Double priceFactor;

    @Schema(description = "Price per seat in this cabin class",
            example = "599.99",
            required = true,
            minimum = "0")
    private Double price;
}
