package com.example.flightapi.airport.service.impl;

import com.example.flightapi.airport.entity.Airport;
import com.example.flightapi.airport.repository.AirportRepository;
import com.example.flightapi.airport.service.AirportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport saveAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    @Override
    public Airport getAirportById(String id) {
        return airportRepository.findById(id).orElse(null);
    }

    @Override
    public Airport getAirportByCode(String airportCode) {
        return airportRepository.findByAirportCode(airportCode);
    }

    @Override
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    @Override
    public void deleteAirport(String id) {
        airportRepository.deleteById(id);
    }

    @Override
    public Airport updateAirport(Airport airport) {
        return airportRepository.save(airport);
    }
}
