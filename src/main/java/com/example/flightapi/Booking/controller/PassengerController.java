package com.example.flightapi.Booking.controller;

import com.example.flightapi.Booking.entity.Passenger;
import com.example.flightapi.Booking.service.PassengerService;
import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Passenger Management", description = "Operations pertaining to flight passengers")
@RestController
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @ApiOperation(value = "Create a new passenger", response = Passenger.class)
    @PostMapping
    public ApiResult createPassenger(
            @ApiParam(value = "Passenger object that needs to be created", required = true)
            @RequestBody Passenger passenger) {
        return ApiResult.success(passengerService.createPassenger(passenger));
    }

    @ApiOperation(value = "Get passenger by ID", response = Passenger.class)
    @GetMapping("/{id}")
    public ApiResult getPassengerById(
            @ApiParam(value = "ID of the passenger to retrieve", required = true)
            @PathVariable String id) {
        return ApiResult.success(passengerService.getPassengerById(id));
    }

    @ApiOperation(value = "Get passengers by booking ID", response = List.class)
    @GetMapping("/booking/{bookingId}")
    public ApiResult getPassengersByBookingId(
            @ApiParam(value = "ID of the booking to retrieve passengers for", required = true)
            @PathVariable String bookingId) {
        return ApiResult.success(passengerService.getPassengersByBookingId(bookingId));
    }

    @ApiOperation(value = "Get all passengers", response = List.class)
    @GetMapping
    public ApiResult getAllPassengers() {
        return ApiResult.success(passengerService.getAllPassengers());
    }

    @ApiOperation(value = "Update a passenger by ID", response = Passenger.class)
    @PutMapping("/{id}")
    public ApiResult updatePassenger(
            @ApiParam(value = "ID of the passenger to update", required = true)
            @PathVariable String id,
            @ApiParam(value = "Updated passenger details", required = true)
            @RequestBody Passenger passenger) {
        passenger.setId(id);
        return ApiResult.success(passengerService.updatePassenger(passenger));
    }

    @ApiOperation(value = "Delete a passenger by ID")
    @DeleteMapping("/{id}")
    public ApiResult deletePassenger(
            @ApiParam(value = "ID of the passenger to delete", required = true)
            @PathVariable String id) {
        passengerService.deletePassenger(id);
        return ApiResult.success();
    }
}
