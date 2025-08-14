package com.example.flightapi.Booking.controller;

import com.example.flightapi.Booking.dto.BookingDTO;
import com.example.flightapi.Booking.entity.Booking;
import com.example.flightapi.Booking.service.BookingService;
import com.example.flightapi.common.exception.handler.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.example.flightapi.common.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.flightapi.common.utils.PageResult;

@Tag(name = "Booking Management", description = "Operations pertaining to flight bookings")
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(summary = "Create a new booking", description = "Creates a new booking for a flight")
    @PostMapping
    public ApiResult createBooking(
            @Parameter(description = "Booking object that needs to be created", required = true)
            @RequestBody Booking resources)  {
        Booking booking = new Booking();
        BeanUtils.copyProperties(resources, booking);
        return ApiResult.success(bookingService.createBooking(booking));
    }

    @Operation(summary = "Get booking by ID", description = "Retrieves a specific booking by its ID")
    @GetMapping("/{id}")
    public ApiResult getBookingById(
            @Parameter(description = "ID of the booking to retrieve", required = true)
            @PathVariable String id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ApiResult::success).orElseGet(() -> ApiResult.success(null));
    }

    @Operation(summary = "Get bookings by flight ID", description = "Retrieves all bookings for a specific flight")
    @GetMapping("/flight/{flightId}")
    public ApiResult getBookingsByFlightId(
            @Parameter(description = "ID of the flight to retrieve bookings for", required = true)
            @PathVariable String flightId) {
        return ApiResult.success(bookingService.getBookingsByFlightId(flightId));
    }

    @Operation(summary = "Get bookings by status", description = "Retrieves all bookings with a specific status")
    @GetMapping("/status/{status}")
    public ApiResult getBookingsByStatus(
            @Parameter(description = "Status of bookings to retrieve (e.g. CONFIRMED, CANCELLED)", required = true)
            @PathVariable String status) {
        return ApiResult.success(bookingService.getBookingsByStatus(status));
    }

    @Operation(summary = "Get bookings by contact email", description = "Retrieves all bookings associated with a specific email")
    @GetMapping("/email/{email}")
    public ApiResult getBookingsByEmail(
            @Parameter(description = "Contact email associated with bookings", required = true)
            @PathVariable String email) {
        return ApiResult.success(bookingService.getBookingsByEmail(email));
    }

    @Operation(summary = "Get bookings within a date-time range", description = "Retrieves all bookings between specified start and end times")
    @GetMapping("/date-range")
    public ApiResult getBookingsByDateRange(
            @Parameter(description = "Start date-time of range (ISO format)", required = true)
            @RequestParam LocalDateTime start,
            @Parameter(description = "End date-time of range (ISO format)", required = true)
            @RequestParam LocalDateTime end) {
        return ApiResult.success(bookingService.getBookingsByDateRange(start, end));
    }

    @Operation(summary = "Get all bookings", description = "Retrieves a list of all bookings")
    @GetMapping
    public ApiResult getAllBookings() {
        return ApiResult.success(bookingService.getAllBookings());
    }

    @Operation(summary = "Get bookings by user ID", description = "Retrieves all bookings for a specific user")
    @GetMapping("/user/{userId}")
    public ApiResult getBookingsByUserId(
            @Parameter(description = "ID of the user to retrieve bookings for", required = true)
            @PathVariable Integer userId) {
        return ApiResult.success(bookingService.getBookingsByUserId(userId));
    }

    @Operation(summary = "Get bookings by user ID and flight ID",
               description = "Retrieves bookings for a specific user and flight combination")
    @GetMapping("/user/{userId}/flight/{flightId}")
    public ApiResult getBookingsByUserIdAndFlightId(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Integer userId,
            @Parameter(description = "ID of the flight", required = true)
            @PathVariable String flightId) {
        return ApiResult.success(bookingService.getBookingsByUserIdAndFlightId(userId, flightId));
    }

    @Operation(summary = "Get current user's bookings within a date-time range",
               description = "Retrieves bookings for the authenticated user between specified start and end times")
    @GetMapping("/my/date-range")
    public ApiResult getBookingsByUserIdAndDateRange(
            @Parameter(description = "Start date and time for search range (ISO format)",
                    required = false, example = "2023-12-25T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End date and time for search range (ISO format)",
                    required = false, example = "2023-12-26T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        return ApiResult.success(bookingService.getBookingsByUserIdAndDateRange(currentUserId, start, end));
    }

    @Operation(summary = "Get current user's bookings within a date-time range with pagination",
               description = "Retrieves paginated bookings for the authenticated user between specified start and end times")
    @GetMapping("/my/date-range/paged")
    public ApiResult getBookingsByUserIdAndDateRangePaged(
            @Parameter(description = "Start date and time for search range (ISO format)",
                    required = false, example = "2023-12-25T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End date and time for search range (ISO format)",
                    required = false, example = "2023-12-26T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @Parameter(description = "Pagination and sorting parameters (page, size, sort). For multiple sort fields use: sort=field1,direction&sort=field2,direction",
                    example = "page=0&size=10&sort=bookingTime,desc&sort=id,asc")
            Pageable pageable) {

        Integer currentUserId = SecurityUtils.getCurrentUserId();

        // 调用服务方法获取分页结果
        PageResult<Booking> pageResult = bookingService.getBookingsByUserIdAndDateRangePaged(
            currentUserId, start, end, pageable);

        return ApiResult.success(pageResult);
    }

    @Operation(summary = "Cancel a booking by ID", description = "Marks a booking as cancelled without deleting it")
    @PutMapping("/{id}/cancel")
    public ApiResult cancelBooking(
            @Parameter(description = "ID of the booking to cancel", required = true)
            @PathVariable String id) {
        bookingService.cancelBooking(id);
        return ApiResult.success();
    }

    @Operation(summary = "Update a booking by ID", description = "Updates an existing booking with new details")
    @PutMapping("/{id}")
    public ApiResult updateBooking(
            @Parameter(description = "ID of the booking to update", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated booking details", required = true)
            @RequestBody Booking resources) {
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            BeanUtils.copyProperties(resources, booking);
            return ApiResult.success(bookingService.updateBooking(booking));
        }
        return ApiResult.success(null);
    }

    @Operation(summary = "Permanently delete a booking by ID", description = "Removes a booking from the system completely")
    @DeleteMapping("/{id}")
    public ApiResult deleteBooking(
            @Parameter(description = "ID of the booking to permanently delete", required = true)
            @PathVariable String id) {
        bookingService.deleteBooking(id);
        return ApiResult.success();
    }
}
