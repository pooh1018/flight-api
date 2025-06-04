package com.example.flightapi.airport.service;

import com.example.flightapi.airport.entity.Airport;
import java.util.List;

public interface AirportService {
    Airport saveAirport(Airport airport);
    Airport getAirportById(String id);
    Airport getAirportByCode(String airportCode);
    List<Airport> getAllAirports();
    void deleteAirport(String id);
    Airport updateAirport(Airport airport);
}
