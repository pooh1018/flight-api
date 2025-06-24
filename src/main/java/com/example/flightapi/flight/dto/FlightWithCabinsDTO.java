package com.example.flightapi.flight.dto;

import com.example.flightapi.cabin.entity.CabinClass;
import com.example.flightapi.flight.entity.Flight;
import lombok.Data;

import java.util.List;

@Data
public class FlightWithCabinsDTO {
    private Flight flight;
    private List<CabinClass> cabins;
}
