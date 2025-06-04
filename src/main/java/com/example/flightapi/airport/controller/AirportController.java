package com.example.flightapi.airport.controller;

import com.example.flightapi.airport.dto.AirportDTO;
import com.example.flightapi.airport.entity.Airport;
import com.example.flightapi.airport.service.AirportService;
import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Airport Management", description = "Provides CRUD APIs for airport management")
@RestController
@RequestMapping("/airports")
public class AirportController {

    @Autowired
    private AirportService airportService;

    @ApiOperation(value = "Create New Airport", notes = "Create a new airport record")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Airport created successfully"),
        @ApiResponse(code = 400, message = "Invalid request parameters"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult createAirport(
            @ApiParam(value = "Airport information", required = true)
            @RequestBody AirportDTO airportDTO) {
        Airport airport = new Airport();
        airport.setAirportId(airportDTO.getAirportId());
        airport.setAirportCode(airportDTO.getAirportCode());
        airport.setAirportName(airportDTO.getAirportName());
        airport.setCity(airportDTO.getCity());
        airport.setCountry(airportDTO.getCountry());
        airportService.saveAirport(airport);
        return ApiResult.success();
    }

    @ApiOperation(value = "Get Airport by ID", notes = "Returns detailed information of a specific airport by ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved airport information"),
        @ApiResponse(code = 404, message = "Airport not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{id}")
    public ApiResult getAirportById(
            @ApiParam(value = "Airport ID", required = true, example = "PEK")
            @PathVariable String id) {
        return ApiResult.success(airportService.getAirportById(id));
    }

    @ApiOperation(value = "Get Airport by Code", notes = "Returns detailed information of a specific airport by code")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved airport information"),
        @ApiResponse(code = 404, message = "Airport not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/code/{code}")
    public ApiResult getAirportByCode(
            @ApiParam(value = "Airport code", required = true, example = "PEK")
            @PathVariable String code) {
        return ApiResult.success(airportService.getAirportByCode(code));
    }

    @ApiOperation(value = "Get All Airports", notes = "Returns a list of all airports in the system")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved airport list"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping
    public ApiResult getAllAirports() {
        return ApiResult.success(airportService.getAllAirports());
    }

    @ApiOperation(value = "Update Airport", notes = "Update information of a specific airport by ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Airport information updated successfully"),
        @ApiResponse(code = 400, message = "Invalid request parameters"),
        @ApiResponse(code = 404, message = "Airport not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/{id}")
    public ApiResult updateAirport(
            @ApiParam(value = "Airport ID", required = true, example = "PEK")
            @PathVariable String id,
            @ApiParam(value = "Updated airport information", required = true)
            @RequestBody AirportDTO airportDTO) {
        Airport airport = airportService.getAirportById(id);
        if (airport != null) {
            airport.setAirportId(airportDTO.getAirportId());
            airport.setAirportCode(airportDTO.getAirportCode());
            airport.setAirportName(airportDTO.getAirportName());
            airport.setCity(airportDTO.getCity());
            airport.setCountry(airportDTO.getCountry());
            return ApiResult.success(airportService.updateAirport(airport));
        }
        return ApiResult.success(null);
    }

    @ApiOperation(value = "Delete Airport", notes = "Delete a specific airport by ID")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Airport deleted successfully"),
        @ApiResponse(code = 404, message = "Airport not found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult deleteAirport(
            @ApiParam(value = "Airport ID to delete", required = true, example = "PEK")
            @PathVariable String id) {
        airportService.deleteAirport(id);
        return ApiResult.success();
    }
}
