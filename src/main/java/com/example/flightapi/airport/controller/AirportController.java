package com.example.flightapi.airport.controller;

import com.example.flightapi.airport.dto.AirportDTO;
import com.example.flightapi.airport.entity.Airport;
import com.example.flightapi.airport.service.AirportService;
import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Airport Management", description = "Provides CRUD APIs for airport management")
@RestController
@RequestMapping("/airports")
public class AirportController {

    @Autowired
    private AirportService airportService;

    @Operation(summary = "Create New Airport", description = "Create a new airport record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Airport created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult createAirport(
            @Parameter(description = "Airport information", required = true)
            @RequestBody AirportDTO airportDTO) {
        Airport airport = new Airport();
        BeanUtils.copyProperties(airportDTO, airport);
        airportService.saveAirport(airport);
        return ApiResult.success();
    }

    @Operation(summary = "Get Airport by ID", description = "Returns detailed information of a specific airport by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved airport information"),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ApiResult getAirportById(
            @Parameter(description = "Airport ID", required = true, example = "PEK")
            @PathVariable String id) {
        return ApiResult.success(airportService.getAirportById(id));
    }

    @Operation(summary = "Get Airport by Code", description = "Returns detailed information of a specific airport by code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved airport information"),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/code/{code}")
    public ApiResult getAirportByCode(
            @Parameter(description = "Airport code", required = true, example = "PEK")
            @PathVariable String code) {
        return ApiResult.success(airportService.getAirportByCode(code));
    }

    @Operation(summary = "Get All Airports", description = "Returns a list of all airports in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved airport list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ApiResult getAllAirports() {
        return ApiResult.success(airportService.getAllAirports());
    }

    @Operation(summary = "Update Airport", description = "Update information of a specific airport by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Airport information updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ApiResult updateAirport(
            @Parameter(description = "Airport ID", required = true, example = "PEK")
            @PathVariable String id,
            @Parameter(description = "Updated airport information", required = true)
            @RequestBody AirportDTO airportDTO) {
        Airport airport = airportService.getAirportById(id);
        if (airport != null) {
            BeanUtils.copyProperties(airportDTO, airport);
            return ApiResult.success(airportService.updateAirport(airport));
        }
        return ApiResult.success(null);
    }

    @Operation(summary = "Delete Airport", description = "Delete a specific airport by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Airport deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult deleteAirport(
            @Parameter(description = "Airport ID to delete", required = true, example = "PEK")
            @PathVariable String id) {
        airportService.deleteAirport(id);
        return ApiResult.success();
    }

    @Operation(summary = "Get Airport by Airport ID", description = "Returns detailed information of a specific airport by numeric airport ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved airport information",
                    content = @Content(schema = @Schema(implementation = Airport.class))),
        @ApiResponse(responseCode = "404", description = "Airport not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/airId/{airportId}")
    public ApiResult getAirportByAirportId(
            @Parameter(description = "Numeric Airport ID", required = true, example = "1001")
            @PathVariable int airportId) {
        return ApiResult.success(airportService.getAirportByAirportId(airportId));
    }
}
