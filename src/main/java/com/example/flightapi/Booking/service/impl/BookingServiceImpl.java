package com.example.flightapi.Booking.service.impl;

import com.example.flightapi.Booking.service.BookingService;
import com.example.flightapi.Booking.entity.Booking;
import com.example.flightapi.Booking.repository.BookingRepository;
import com.example.flightapi.common.exception.EntityNotFoundException;
import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.flight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FlightService flightService;

    @Override
    public Booking createBooking(Booking booking) {

        // Validate flight availability
        Flight flight = flightService.getFlightById(booking.getFlightId());
        if (flight == null || flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("Flight not available for booking");
        }

        if (booking.getStatus() == null) {
            booking.setStatus("PENDING");
        }

        // Calculate total price based on flight base price and number of passengers
        BigDecimal basePrice = BigDecimal.valueOf(flight.getBasePrice());
        BigDecimal passengerCount = BigDecimal.valueOf(booking.getPassengers().size());
        BigDecimal totalPrice = basePrice.multiply(passengerCount);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("CONFIRMED");
        booking.setBookingTime(LocalDateTime.now());

        // Update available seats
        flight.setAvailableSeats(flight.getAvailableSeats() - booking.getPassengers().size());
        flightService.updateFlight(flight);

        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> getBookingById(String id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Optional<Booking> getBookingByIdAndEmail(String id, String email) {
        return bookingRepository.findByIdAndContactEmail(id, email);
    }

    @Override
    public Optional<Booking> getBookingByReference(String reference) {
        return bookingRepository.findByReference(reference);
    }

    @Override
    public List<Booking> getBookingsByFlightId(String flightId) {
        return bookingRepository.findByFlightId(flightId);
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByContactEmail(email);
    }

    @Override
    public List<Booking> getBookingsByDateRange(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findByBookingTimeBetween(start, end);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public void cancelBooking(String id) {
        Optional<Booking> bookingOpt = getBookingById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // Update booking status
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);

            // Restore available seats
            Flight flight = flightService.getFlightById(booking.getFlightId());
            if (flight != null) {
                flight.setAvailableSeats(flight.getAvailableSeats() + booking.getPassengers().size());
                flightService.updateFlight(flight);
            }
        }
    }

    @Override
    public Booking updateBooking(Booking booking) {
        if (!bookingRepository.existsById(booking.getId())) {
            throw new EntityNotFoundException(Booking.class, "id", booking.getId());
        }
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }
}
