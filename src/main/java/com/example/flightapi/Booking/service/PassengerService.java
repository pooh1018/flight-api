package com.example.flightapi.Booking.service;

import com.example.flightapi.Booking.entity.Passenger;
import java.util.List;
import java.util.Optional;

public interface PassengerService {
    Passenger createPassenger(Passenger passenger);
    Optional<Passenger> getPassengerById(String id);
    List<Passenger> getPassengersByBookingId(String bookingId);
    List<Passenger> getAllPassengers();
    Passenger updatePassenger(Passenger passenger);
    void deletePassenger(String id);
}
