package com.example.flightapi.Booking.service;

import com.example.flightapi.Booking.dto.BookingDTO;
import com.example.flightapi.Booking.entity.Booking;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(Booking booking);
    Optional<Booking> getBookingById(String id);
    Optional<Booking> getBookingByIdAndEmail(String id, String email);
    Optional<Booking> getBookingByReference(String reference);
    List<Booking> getBookingsByFlightId(String flightId);
    List<Booking> getBookingsByUserId(int userId);
    List<Booking> getBookingsByUserIdAndFlightId(int userId, String flightId);
    List<Booking> getBookingsByUserIdAndDateRange(int userId, LocalDateTime start, LocalDateTime end);
    List<Booking> getBookingsByStatus(String status);
    List<Booking> getBookingsByEmail(String email);
    List<Booking> getBookingsByDateRange(LocalDateTime start, LocalDateTime end);
    List<Booking> getAllBookings();
    void cancelBooking(String id);
    void deleteBooking(String id);
    Booking updateBooking(Booking booking);
}
