package com.example.flightapi.flight.controller;

import com.example.flightapi.common.exception.handler.ApiResult;
import com.example.flightapi.flight.dto.FlightDTO;
import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.flight.service.FlightService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @ApiOperation(value = "Create a new flight", response = Flight.class)
    @PostMapping
    public ApiResult createFlight(
            @ApiParam(value = "Flight object that needs to be created", required = true)
            @RequestBody FlightDTO flightDTO) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setDepartureAirportId(flightDTO.getDepartureAirportId());
        flight.setArrivalAirportId(flightDTO.getArrivalAirportId());
        flight.setDepartureTime(flightDTO.getDepartureTime());
        flight.setArrivalTime(flightDTO.getArrivalTime());
        flight.setBasePrice(flightDTO.getBasePrice());
        flight.setStatus(flightDTO.getStatus());
        flight.setAvailableSeats(flightDTO.getAvailableSeats());
        flightService.saveFlight(flight);
        return ApiResult.success();
    }

    @ApiOperation(value = "Get flight by ID", response = Flight.class)
    @GetMapping("/{id}")
    public ApiResult getFlightById(
            @ApiParam(value = "ID of flight to return", required = true)
            @PathVariable String id) {
        return ApiResult.success(flightService.getFlightById(id));
    }

    @ApiOperation(value = "Get flight by flight number", response = Flight.class)
    @GetMapping("/number/{flightNumber}")
    public ApiResult getFlightByNumber(
            @ApiParam(value = "Flight number to search by", required = true)
            @PathVariable String flightNumber) {
        return ApiResult.success(flightService.getFlightByNumber(flightNumber));
    }

    @ApiOperation(value = "Get flights by departure airport ID", response = List.class)
    @GetMapping("/departure/{airportId}")
    public ApiResult getFlightsByDepartureAirport(
            @ApiParam(value = "ID of departure airport", required = true)
            @PathVariable Integer airportId) {
        return ApiResult.success(flightService.getFlightsByDepartureAirport(airportId));
    }

    @ApiOperation(value = "Get flights by arrival airport ID", response = List.class)
    @GetMapping("/arrival/{airportId}")
    public ApiResult getFlightsByArrivalAirport(
            @ApiParam(value = "ID of arrival airport", required = true)
            @PathVariable Integer airportId) {
        return ApiResult.success(flightService.getFlightsByArrivalAirport(airportId));
    }

    @GetMapping("/route")
    public ApiResult getFlightsByRoute(
            @RequestParam Integer departureAirportId,
            @RequestParam Integer arrivalAirportId) {
        return ApiResult.success(flightService.getFlightsByRoute(departureAirportId, arrivalAirportId));
    }

    @ApiOperation(value = "Get all flights", response = List.class)
    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @ApiOperation(value = "Update an existing flight", response = Flight.class)
    @PutMapping("/{id}")
    public ApiResult updateFlight(
            @ApiParam(value = "ID of flight that needs to be updated", required = true)
            @PathVariable String id,
            @ApiParam(value = "Updated flight object", required = true)
            @RequestBody FlightDTO flightDTO) {
        Flight flight = flightService.getFlightById(id);
        if (flight != null) {
            flight.setFlightNumber(flightDTO.getFlightNumber());
            flight.setDepartureAirportId(flightDTO.getDepartureAirportId());
            flight.setArrivalAirportId(flightDTO.getArrivalAirportId());
            flight.setDepartureTime(flightDTO.getDepartureTime());
            flight.setArrivalTime(flightDTO.getArrivalTime());
            flight.setBasePrice(flightDTO.getBasePrice());
            flight.setStatus(flightDTO.getStatus());
            flight.setAvailableSeats(flightDTO.getAvailableSeats());
            return ApiResult.success(flightService.updateFlight(flight));
        }
        return ApiResult.success(null);
    }

    @ApiOperation(value = "Delete a flight by ID")
    @DeleteMapping("/{id}")
    public ApiResult deleteFlight(
            @ApiParam(value = "ID of flight to delete", required = true)
            @PathVariable String id) {
        flightService.deleteFlight(id);
        return ApiResult.success();
    }
}
