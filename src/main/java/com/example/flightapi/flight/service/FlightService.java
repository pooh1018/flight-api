package com.example.flightapi.flight.service;

import com.example.flightapi.flight.entity.Flight;
import java.util.List;

public interface FlightService {
    Flight saveFlight(Flight flight);
    Flight getFlightById(String id);
    Flight getFlightByNumber(String flightNumber);
    List<Flight> getFlightsByDepartureAirport(Integer airportId);
    List<Flight> getFlightsByArrivalAirport(Integer airportId);
    List<Flight> getFlightsByRoute(Integer departureAirportId, Integer arrivalAirportId);
    List<Flight> getAllFlights();
    void deleteFlight(String id);
    Flight updateFlight(Flight flight);
}
