package com.example.flightapi.Booking.controller;

import com.example.flightapi.Booking.entity.Passenger;
import com.example.flightapi.Booking.service.PassengerService;
import com.example.flightapi.common.exception.handler.ApiResult;
import com.example.flightapi.common.utils.SecurityUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Passenger Management", description = "Operations pertaining to flight passengers including creation, retrieval, update and deletion")
@RestController
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @Operation(summary = "Create a new passenger",
               description = "Creates a new passenger record with all required details including name, contact information and travel documents")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Passenger created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid passenger data provided"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ApiResult createPassenger(
            @Parameter(description = "Passenger details including name, contact info, passport details and other required information",
                      required = true)
            @RequestBody Passenger passenger) {
        passengerService.createPassenger(passenger);
        return ApiResult.success();
    }

    @Operation(summary = "Get passenger by ID",
              description = "Retrieves complete passenger details including contact information and booking references")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Passenger details retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid ID format"),
        @ApiResponse(responseCode = "404", description = "Passenger not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ApiResult getPassengerById(
            @Parameter(description = "ID of the passenger to retrieve",
                      required = true,
                      example = "PAX-12345")
            @PathVariable ObjectId id) {
        return ApiResult.success(passengerService.getPassengerById(id));
    }

    @Operation(summary = "Get passengers by booking ID", description = "Retrieves all passengers associated with a booking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved passengers"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/listByUserId")
    public ApiResult getPassengersByUserId() {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        return ApiResult.success(passengerService.getPassengersByUserId(currentUserId));
    }

    @Operation(summary = "Get all passengers", description = "Retrieves a list of all passengers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved passenger list")
    })
    @GetMapping
    public ApiResult getAllPassengers() {
        return ApiResult.success(passengerService.getAllPassengers());
    }

    @Operation(summary = "Update passenger details",
              description = "Updates all modifiable passenger information including contact details and special requests")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Passenger updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid passenger data or ID format"),
        @ApiResponse(responseCode = "404", description = "Passenger not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ApiResult updatePassenger(
            @Parameter(description = "Updated passenger object containing all modified fields",
                      required = true)
            @RequestBody Passenger passenger) {
        passengerService.updatePassenger(passenger);
        return ApiResult.success();
    }

    @Operation(summary = "Delete a passenger by ID", description = "Deletes a passenger record by their ID")
    @DeleteMapping("/{id}")
    public ApiResult deletePassenger(
            @Parameter(description = "ID of the passenger to delete", required = true)
            @PathVariable ObjectId id) {
        passengerService.deletePassenger(id);
        return ApiResult.success();
    }
}
