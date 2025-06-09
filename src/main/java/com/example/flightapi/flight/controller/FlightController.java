package com.example.flightapi.flight.controller;

import com.example.flightapi.common.exception.handler.ApiResult;
import com.example.flightapi.flight.dto.FlightDTO;
import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.flight.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
@Tag(name = "Flight Management", description = "Operations pertaining to flights in Flight API")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Operation(summary = "Create a new flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Flight created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ApiResult createFlight(
            @Parameter(description = "Flight object that needs to be created", required = true)
            @RequestBody FlightDTO flightDTO) {
        Flight flight = new Flight();
        BeanUtils.copyProperties(flightDTO, flight);
        flightService.saveFlight(flight);
        return ApiResult.success();
    }

    @Operation(summary = "Get flight by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved flight"),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ApiResult getFlightById(
            @Parameter(description = "ID of flight to return", required = true)
            @PathVariable String id) {
        return ApiResult.success(flightService.getFlightById(id));
    }

    @Operation(summary = "Get flight by flight number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved flight"),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/number/{flightNumber}")
    public ApiResult getFlightByNumber(
            @Parameter(description = "Flight number to search by", required = true)
            @PathVariable String flightNumber) {
        return ApiResult.success(flightService.getFlightByNumber(flightNumber));
    }

    @Operation(summary = "Get flights by departure airport ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved flights"),
        @ApiResponse(responseCode = "204", description = "No flights found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/departure/{airportId}")
    public ApiResult getFlightsByDepartureAirport(
            @Parameter(description = "ID of departure airport", required = true)
            @PathVariable Integer airportId) {
        return ApiResult.success(flightService.getFlightsByDepartureAirport(airportId));
    }

    @Operation(summary = "Get flights by arrival airport ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved flights"),
        @ApiResponse(responseCode = "204", description = "No flights found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/arrival/{airportId}")
    public ApiResult getFlightsByArrivalAirport(
            @Parameter(description = "ID of arrival airport", required = true)
            @PathVariable Integer airportId) {
        return ApiResult.success(flightService.getFlightsByArrivalAirport(airportId));
    }

    @Operation(summary = "Get flights by route")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved flights"),
        @ApiResponse(responseCode = "204", description = "No flights found for this route"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/route")
    public ApiResult getFlightsByRoute(
            @Parameter(description = "ID of departure airport", required = true) @RequestParam Integer departureAirportId,
            @Parameter(description = "ID of arrival airport", required = true) @RequestParam Integer arrivalAirportId) {
        return ApiResult.success(flightService.getFlightsByRoute(departureAirportId, arrivalAirportId));
    }

    @Operation(summary = "Get all flights")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all flights"),
        @ApiResponse(responseCode = "204", description = "No flights available"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @Operation(summary = "Update an existing flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ApiResult updateFlight(
            @Parameter(description = "ID of flight that needs to be updated", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated flight object", required = true)
            @RequestBody FlightDTO flightDTO) {
        Flight flight = flightService.getFlightById(id);
        if (flight != null) {
            BeanUtils.copyProperties(flightDTO, flight);
            return ApiResult.success(flightService.updateFlight(flight));
        }
        return ApiResult.success(null);
    }

    @Operation(summary = "Delete a flight by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Flight not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ApiResult deleteFlight(
            @Parameter(description = "ID of flight to delete", required = true)
            @PathVariable String id) {
        flightService.deleteFlight(id);
        return ApiResult.success();
    }
}
