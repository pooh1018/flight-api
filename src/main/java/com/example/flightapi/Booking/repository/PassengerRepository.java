package com.example.flightapi.Booking.repository;

import com.example.flightapi.Booking.entity.Passenger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends MongoRepository<Passenger, String> {
    List<Passenger> findByBookingId(String bookingId);
}
