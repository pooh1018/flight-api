package com.example.flightapi.Booking.controller;

import com.example.flightapi.Booking.entity.Booking;
import com.example.flightapi.Booking.service.BookingService;
import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Api(tags = "Booking Management", description = "Operations pertaining to flight bookings")
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @ApiOperation(value = "Create a new booking", response = Booking.class)
    @PostMapping
    public ApiResult createBooking(
            @ApiParam(value = "Booking object that needs to be created", required = true)
            @RequestBody Booking resources)  {
        Booking booking = new Booking();
        booking.setFlightId(resources.getFlightId());
        booking.setPassengers(resources.getPassengers());
        booking.setTotalPrice(resources.getTotalPrice());
        booking.setStatus(resources.getStatus());
        booking.setBookingTime(LocalDateTime.now());
        booking.setPaymentMethod(resources.getPaymentMethod());
        booking.setContactEmail(resources.getContactEmail());
        booking.setContactPhone(resources.getContactPhone());
        bookingService.createBooking(booking);
        return ApiResult.success();
    }

    @ApiOperation(value = "Get booking by ID", response = Booking.class)
    @GetMapping("/{id}")
    public ApiResult getBookingById(
            @ApiParam(value = "ID of the booking to retrieve", required = true)
            @PathVariable String id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ApiResult::success).orElseGet(() -> ApiResult.success(null));
    }

    @ApiOperation(value = "Get booking by ID and email", response = Booking.class)
    @GetMapping("/{id}/email/{email}")
    public ApiResult getBookingByIdAndEmail(
            @ApiParam(value = "ID of the booking to retrieve", required = true)
            @PathVariable String id,
            @ApiParam(value = "Contact email associated with the booking", required = true)
            @PathVariable String email) {
        Optional<Booking> booking = bookingService.getBookingByIdAndEmail(id, email);
        return booking.map(ApiResult::success).orElseGet(() -> ApiResult.success(null));
    }

    @ApiOperation(value = "Get bookings by flight ID", response = List.class)
    @GetMapping("/flight/{flightId}")
    public ApiResult getBookingsByFlightId(
            @ApiParam(value = "ID of the flight to retrieve bookings for", required = true)
            @PathVariable String flightId) {
        return ApiResult.success(bookingService.getBookingsByFlightId(flightId));
    }

    @ApiOperation(value = "Get bookings by status", response = List.class)
    @GetMapping("/status/{status}")
    public ApiResult getBookingsByStatus(
            @ApiParam(value = "Status of bookings to retrieve (e.g. CONFIRMED, CANCELLED)", required = true)
            @PathVariable String status) {
        return ApiResult.success(bookingService.getBookingsByStatus(status));
    }

    @ApiOperation(value = "Get bookings by contact email", response = List.class)
    @GetMapping("/email/{email}")
    public ApiResult getBookingsByEmail(
            @ApiParam(value = "Contact email associated with bookings", required = true)
            @PathVariable String email) {
        return ApiResult.success(bookingService.getBookingsByEmail(email));
    }

    @ApiOperation(value = "Get bookings within a date-time range", response = List.class)
    @GetMapping("/date-range")
    public ApiResult getBookingsByDateRange(
            @ApiParam(value = "Start date-time of range (ISO format)", required = true)
            @RequestParam LocalDateTime start,
            @ApiParam(value = "End date-time of range (ISO format)", required = true)
            @RequestParam LocalDateTime end) {
        return ApiResult.success(bookingService.getBookingsByDateRange(start, end));
    }

    @ApiOperation(value = "Get all bookings", response = List.class)
    @GetMapping
    public ApiResult getAllBookings() {
        return ApiResult.success(bookingService.getAllBookings());
    }

    @ApiOperation(value = "Cancel a booking by ID (marks as cancelled)")
    @PutMapping("/{id}/cancel")
    public ApiResult cancelBooking(
            @ApiParam(value = "ID of the booking to cancel", required = true)
            @PathVariable String id) {
        bookingService.cancelBooking(id);
        return ApiResult.success();
    }

    @ApiOperation(value = "Update a booking by ID", response = Booking.class)
    @PutMapping("/{id}")
    public ApiResult updateBooking(
            @ApiParam(value = "ID of the booking to update", required = true)
            @PathVariable String id,
            @ApiParam(value = "Updated booking details", required = true)
            @RequestBody Booking resources) {
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setFlightId(resources.getFlightId());
            booking.setPassengers(resources.getPassengers());
            booking.setTotalPrice(resources.getTotalPrice());
            booking.setStatus(resources.getStatus());
            booking.setBookingTime(resources.getBookingTime());
            booking.setPaymentMethod(resources.getPaymentMethod());
            booking.setContactEmail(resources.getContactEmail());
            booking.setContactPhone(resources.getContactPhone());
            return ApiResult.success(bookingService.updateBooking(booking));
        }
        return ApiResult.success(null);
    }

    @ApiOperation(value = "Permanently delete a booking by ID")
    @DeleteMapping("/{id}")
    public ApiResult deleteBooking(
            @ApiParam(value = "ID of the booking to permanently delete", required = true)
            @PathVariable String id) {
        bookingService.deleteBooking(id);
        return ApiResult.success();
    }
}
