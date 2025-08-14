package com.example.flightapi.flight.controller;

import com.example.flightapi.cabin.entity.CabinClass;
import com.example.flightapi.common.exception.handler.ApiResult;
import com.example.flightapi.common.utils.PageResult;
import com.example.flightapi.flight.dto.FlightWithCabinsDTO;
import com.example.flightapi.flight.dto.SearchFlightRequestDTO;
import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.flight.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/flights")
@Tag(name = "Flight Management", description = "APIs for managing and searching flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    /**
     * Get flight by ID with cabin class information
     */
    @GetMapping("/{id}/with-cabins")
    @Operation(summary = "Get flight with cabin classes by ID",
               description = "Retrieves a flight and its associated cabin classes by flight ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight found with cabin classes",
                     content = @Content(schema = @Schema(implementation = Flight.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ApiResult getFlightWithCabinClasses(
            @Parameter(description = "ID of the flight to retrieve", required = true)
            @PathVariable String id) {
        Flight flight = flightService.findFlightWithCabinClasses(id);
        return ApiResult.success(flight);
    }

    /**
     * Search flights with cabin class information
     */
    @GetMapping("/with-cabins")
    @Operation(summary = "Search flights with cabin classes",
               description = "Searches for flights with their associated cabin classes based on route and optional date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of flights with cabin classes",
                     content = @Content(schema = @Schema(implementation = Flight.class)))
    })
    public ApiResult getFlightsWithCabinClasses(
            @Parameter(description = "Departure id", required = true, example = "1001")
            @RequestParam Integer departureAirportId,
            @Parameter(description = "Arrival id", required = true, example = "1002")
            @RequestParam Integer destinationAirportId,
            @Parameter(description = "Start date and time for search range (ISO format)",
                      required = false, example = "2023-12-25T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time for search range (ISO format)",
                      required = false, example = "2023-12-26T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Flight> flights = flightService.findFlightsWithCabinClasses(
                departureAirportId, destinationAirportId, startDate, endDate);
        return ApiResult.success(flights);
    }

    /**
     * Search flights with available cabin classes
     */
    @GetMapping("/with-available-cabins")
    @Operation(summary = "Search flights with available cabin classes",
               description = "Searches for flights that have available seats in their cabin classes based on route and optional date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of flights with available cabin classes",
                     content = @Content(schema = @Schema(implementation = Flight.class)))
    })
    public ApiResult getFlightsWithAvailableCabinClasses(
            @Parameter(description = "Departure id", required = true, example = "1001")
            @RequestParam Integer departureAirportId,
            @Parameter(description = "Arrival id", required = true, example = "1002")
            @RequestParam Integer destinationAirportId,
            @Parameter(description = "Start date and time for search range (ISO format)",
                      required = false, example = "2023-12-25T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date and time for search range (ISO format)",
                      required = false, example = "2023-12-26T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Flight> flights = flightService.findFlightsWithAvailableCabinClasses(
                departureAirportId, destinationAirportId, startDate, endDate);
        return ApiResult.success(flights);
    }

    /**
     * Get all flights with cabin class information (paginated)
     */
    @GetMapping("/with-cabins/paged")
    @Operation(summary = "Get all flights with cabin classes (paginated)",
               description = "Retrieves all flights with their associated cabin classes in paginated format")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paginated list of flights with cabin classes",
                     content = @Content(schema = @Schema(implementation = PageResult.class)))
    })
    public ApiResult getAllFlightsWithCabins(
            @Parameter(description = "Pagination parameters (page, size, sort)")
            Pageable pageable) {
        PageResult<FlightWithCabinsDTO> flightPage = flightService.getAllFlightsWithCabins(pageable);
        return ApiResult.success(flightPage);
    }

    /**
     * Advanced search flights with cabin classes (paginated)
     */
    @PostMapping("/with-cabins/search/paged")
    @Operation(summary = "Advanced search flights with cabin classes (paginated)",
               description = "Searches flights with cabin classes by route and optional date range in paginated format. Search criteria should be provided in request body.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paginated list of matching flights with cabin classes",
                     content = @Content(schema = @Schema(implementation = PageResult.class)))
    })
    public ApiResult searchFlightsWithCabinClassesByPage(
            @Parameter(description = "Search criteria")
            @RequestBody SearchFlightRequestDTO searchRequest,
            @Parameter(description = "Pagination parameters (page, size, sort)")
            Pageable pageable) {
        return ApiResult.success(flightService.findFlightsWithCabinClassesByPage(
                searchRequest.getDepartureAirportId(),
                searchRequest.getDestinationAirportId(),
                searchRequest.getStartDateAsLocalDateTime(),
                searchRequest.getEndDateAsLocalDateTime(),
                pageable));
    }

    /**
     * Get flight by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get flight by ID",
               description = "Retrieves a flight by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight found",
                     content = @Content(schema = @Schema(implementation = Flight.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ApiResult getFlightById(
            @Parameter(description = "ID of the flight to retrieve", required = true)
            @PathVariable String id) {
        return ApiResult.success(flightService.getFlightById(id));
    }

    /**
     * Update flight information
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update flight",
               description = "Updates flight information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight updated",
                     content = @Content(schema = @Schema(implementation = Flight.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ApiResult updateFlight(
            @Parameter(description = "ID of the flight to update", required = true)
            @PathVariable String id,
            @RequestBody Flight flight) {
        flight.setId(id);
        return ApiResult.success(flightService.updateFlight(flight));
    }

    /**
     * Get cabin classes for a flight
     */
    @GetMapping("/{flightId}/cabins")
    @Operation(summary = "Get cabin classes for flight",
               description = "Retrieves all cabin classes for a specific flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of cabin classes",
                     content = @Content(schema = @Schema(implementation = CabinClass.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public ApiResult getCabinClassesByFlightId(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable String flightId) {
        return ApiResult.success(flightService.getCabinClassesByFlightId(flightId));
    }

    /**
     * Update cabin class availability
     */
    @PutMapping("/{flightId}/cabins/{classType}/availability")
    @Operation(summary = "Update cabin class availability",
               description = "Updates the available seat count for a cabin class")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cabin class updated",
                     content = @Content(schema = @Schema(implementation = CabinClass.class))),
        @ApiResponse(responseCode = "404", description = "Flight or cabin class not found")
    })
    public ApiResult updateCabinClassAvailability(
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable String flightId,
            @Parameter(description = "Type of cabin class", required = true)
            @PathVariable int classType,
            @Parameter(description = "New available seat count", required = true)
            @RequestParam int seatCount) {
        return ApiResult.success(flightService.updateCabinClassAvailability(
                flightId, classType, seatCount));
    }
}
