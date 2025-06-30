package com.example.flightapi.Booking.service;

import com.example.flightapi.Booking.entity.Passenger;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface PassengerService {

    Optional<Passenger> getPassengerById(ObjectId passengerId);

    List<Passenger> getPassengersByUserId(int userId);

    List<Passenger> getAllPassengers();

    void updatePassenger(Passenger passenger);

    void createPassenger(Passenger passenger);

    void deletePassenger(ObjectId id);
}
