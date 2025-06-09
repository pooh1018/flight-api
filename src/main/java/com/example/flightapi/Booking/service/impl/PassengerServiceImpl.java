package com.example.flightapi.Booking.service.impl;

import com.example.flightapi.Booking.entity.Passenger;
import com.example.flightapi.Booking.repository.PassengerRepository;
import com.example.flightapi.Booking.service.PassengerService;
import com.example.flightapi.common.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public Passenger createPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public Optional<Passenger> getPassengerById(String id) {
        return passengerRepository.findById(id);
    }

    @Override
    public List<Passenger> getPassengersByBookingId(String bookingId) {
        return passengerRepository.findByBookingId(bookingId);
    }

    @Override
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    @Override
    public Passenger updatePassenger(Passenger passenger) {
        if (!passengerRepository.existsById(passenger.getId())) {
            throw new EntityNotFoundException(Passenger.class, "id", passenger.getId());
        }
        return passengerRepository.save(passenger);
    }

    @Override
    public void deletePassenger(String id) {
        if (!passengerRepository.existsById(id)) {
            throw new EntityNotFoundException(Passenger.class, "id", id);
        }
        passengerRepository.deleteById(id);
    }
}
