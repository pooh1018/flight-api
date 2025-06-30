package com.example.flightapi.cabin.controller;

import com.example.flightapi.cabin.entity.CabinClass;
import com.example.flightapi.cabin.service.CabinClassService;
import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cabin-classes")
@Tag(name = "Cabin Class Management", description = "APIs for managing flight cabin classes")
public class CabinClassController {

    @Autowired
    private CabinClassService cabinClassService;

    @PostMapping
    @Operation(summary = "Create a new cabin class",
               description = "Creates a new cabin class for a specific flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cabin class created successfully",
                     content = @Content(schema = @Schema(implementation = CabinClass.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ApiResult createCabinClass(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Cabin class details to be created",
                required = true,
                content = @Content(schema = @Schema(implementation = CabinClass.class))
            ) CabinClass cabinClass) {
        cabinClassService.createCabinClass(cabinClass);
        return ApiResult.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cabin class by ID",
               description = "Retrieves a cabin class by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cabin class found",
                     content = @Content(schema = @Schema(implementation = CabinClass.class))),
        @ApiResponse(responseCode = "404", description = "Cabin class not found")
    })
    public ApiResult getCabinClassById(
            @Parameter(description = "ID of the cabin class to retrieve", required = true)
            @PathVariable String id) {
        Optional<CabinClass> cabinClass = cabinClassService.getCabinClassById(id);
        return ApiResult.success(cabinClass);
    }

    @GetMapping("/flight/{flightId}")
    @Operation(summary = "Get all cabin classes for a flight",
               description = "Retrieves all cabin classes associated with a specific flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of cabin classes",
                     content = @Content(schema = @Schema(implementation = CabinClass.class)))
    })
    public ApiResult getCabinClassesByFlightId(
            @Parameter(description = "ID of the flight to get cabin classes for", required = true)
            @PathVariable String flightId) {
        List<CabinClass> cabinClasses = cabinClassService.getCabinClassesByFlightId(flightId);
        return ApiResult.success(cabinClasses);
    }

    @GetMapping("/flight/{flightId}/available")
    @Operation(summary = "Get available cabin classes for a flight",
               description = "Retrieves cabin classes with available seats for a specific flight")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of available cabin classes",
                     content = @Content(schema = @Schema(implementation = CabinClass.class)))
    })
    public ApiResult getAvailableCabinClassesByFlightId(
            @Parameter(description = "ID of the flight to get available cabin classes for", required = true)
            @PathVariable String flightId) {
        List<CabinClass> cabinClasses = cabinClassService.getAvailableCabinClassesByFlightId(flightId);
        return ApiResult.success(cabinClasses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a cabin class",
               description = "Updates an existing cabin class with new information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cabin class updated successfully",
                     content = @Content(schema = @Schema(implementation = CabinClass.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Cabin class not found")
    })
    public ApiResult updateCabinClass(
            @Parameter(description = "ID of the cabin class to update", required = true)
            @PathVariable String id,
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated cabin class details",
                required = true,
                content = @Content(schema = @Schema(implementation = CabinClass.class))
            ) CabinClass cabinClass) {
        cabinClass.setId(id);
        cabinClassService.updateCabinClass(cabinClass);
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cabin class",
               description = "Deletes a cabin class by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cabin class deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Cabin class not found")
    })
    public ApiResult deleteCabinClass(
            @Parameter(description = "ID of the cabin class to delete", required = true)
            @PathVariable String id) {
        cabinClassService.deleteCabinClass(id);
        return ApiResult.success();
    }

    @PostMapping("/{id}/book")
    @Operation(summary = "Book seats in a cabin class",
               description = "Books a specified number of seats in a cabin class")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seats booked successfully",
                     content = @Content(schema = @Schema(implementation = CabinClass.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Cabin class not found"),
        @ApiResponse(responseCode = "409", description = "Not enough available seats")
    })
    public ApiResult bookSeats(
            @Parameter(description = "ID of the cabin class to book seats in", required = true)
            @PathVariable String id,
            @Parameter(description = "Number of seats to book", required = true)
            @RequestParam int numSeats) {
        cabinClassService.bookSeats(id, numSeats);
        return ApiResult.success();
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel seat booking in a cabin class",
               description = "Cancels booking for a specified number of seats in a cabin class")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Booking canceled successfully",
                     content = @Content(schema = @Schema(implementation = CabinClass.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Cabin class not found"),
        @ApiResponse(responseCode = "409", description = "Cannot cancel more seats than total seats")
    })
    public ApiResult cancelBooking(
            @Parameter(description = "ID of the cabin class to cancel booking in", required = true)
            @PathVariable String id,
            @Parameter(description = "Number of seats to cancel booking for", required = true)
            @RequestParam int numSeats) {
        cabinClassService.cancelBooking(id, numSeats);
        return ApiResult.success();
    }
}
