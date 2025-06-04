package com.example.flightapi.booking.repository;

import com.example.flightapi.booking.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PassengerRepository extends MongoRepository<Booking, String> {
}
