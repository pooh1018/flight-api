package com.example.flightapi.Booking.repository;

import com.example.flightapi.Booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByFlightId(String flightId);
    List<Booking> findByUserId(int userId);
    List<Booking> findByStatus(String status);
    List<Booking> findByContactEmail(String email);
    List<Booking> findByUserIdAndFlightId(int userId, String flightId);
    List<Booking> findByUserIdAndBookingTimeBetween(int userId, LocalDateTime start, LocalDateTime end);
    Page<Booking> findByUserIdAndBookingTimeBetween(int userId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    List<Booking> findByBookingTimeBetween(LocalDateTime start, LocalDateTime end);
    Optional<Booking> findByIdAndContactEmail(String id, String email);
    Optional<Booking> findByReference(String reference);
}
