package com.example.flightapi.flight.service.impl;

import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.flight.repository.FlightRepository;
import com.example.flightapi.flight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight getFlightById(String id) {
        return flightRepository.findById(id).orElse(null);
    }

    @Override
    public Flight getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber);
    }

    @Override
    public List<Flight> getFlightsByDepartureAirport(Integer airportId) {
        return flightRepository.findByDepartureAirportId(airportId);
    }

    @Override
    public List<Flight> getFlightsByArrivalAirport(Integer airportId) {
        return flightRepository.findByArrivalAirportId(airportId);
    }

    @Override
    public List<Flight> getFlightsByRoute(Integer departureAirportId, Integer arrivalAirportId) {
        return flightRepository.findByDepartureAirportIdAndArrivalAirportId(departureAirportId, arrivalAirportId);
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public void deleteFlight(String id) {
        flightRepository.deleteById(id);
    }

    @Override
    public Flight updateFlight(Flight flight) {
        return flightRepository.save(flight);
    }
}
