package com.example.flightapi.flight.repository;

import com.example.flightapi.flight.entity.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FlightRepository extends MongoRepository<Flight, String> {
    Flight findByFlightNumber(String flightNumber);
    List<Flight> findByDepartureAirportId(Integer departureAirportId);
    List<Flight> findByArrivalAirportId(Integer arrivalAirportId);
    List<Flight> findByDepartureAirportIdAndArrivalAirportId(Integer departureAirportId, Integer arrivalAirportId);
}
