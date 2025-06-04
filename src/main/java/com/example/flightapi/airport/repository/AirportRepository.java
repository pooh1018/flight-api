package com.example.flightapi.airport.repository;

import com.example.flightapi.airport.entity.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirportRepository extends MongoRepository<Airport, String> {
    Airport findByAirportCode(String airportCode);
    Airport findByAirportId(Integer airportId);
}
