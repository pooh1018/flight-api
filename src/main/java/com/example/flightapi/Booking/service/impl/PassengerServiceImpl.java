package com.example.flightapi.Booking.service.impl;

import com.example.flightapi.Booking.entity.Passenger;
import com.example.flightapi.Booking.repository.PassengerRepository;
import com.example.flightapi.Booking.service.PassengerService;
import com.example.flightapi.User.entity.User;
import com.example.flightapi.User.repository.UserRepository;
import com.example.flightapi.common.exception.EntityExistException;
import com.example.flightapi.common.exception.EntityNotFoundException;
import com.example.flightapi.common.utils.SecurityUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void createPassenger(Passenger passenger) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();

        // 检查邮箱是否已存在
        if (passengerRepository.existsByUserIdAndEmail(currentUserId, passenger.getEmail())) {
            throw new EntityExistException(Passenger.class, "email", passenger.getEmail());
        }

        // 检查手机号是否已存在
        if (passengerRepository.existsByUserIdAndPhone(currentUserId, passenger.getPhone())) {
            throw new EntityExistException(Passenger.class, "phone", passenger.getPhone());
        }

        // 设定当前用户
        passenger.setUserId(currentUserId);

        // 标记是否是注册用户
        User user = userRepository.findByEmail(passenger.getEmail());
        if (user == null) {
            user = userRepository.findByPhone(passenger.getPhone());
        }

        if (user != null) {
            passenger.setPassengerUserId(user.getUserId());
        }

        passengerRepository.save(passenger);
    }

    @Override
    public List<Passenger> getPassengersByUserId(int userId) {
        return passengerRepository.findByUserId(userId);
    }

    @Override
    public Optional<Passenger> getPassengerById(ObjectId passengerId) {
        return passengerRepository.findById(passengerId);
    }

    @Override
    public void updatePassenger(Passenger passenger) {

        Passenger oldPassenger = passengerRepository.findById(passenger.getId()).orElse(null);
        if (oldPassenger == null) {
            throw new EntityNotFoundException(Passenger.class, "id", passenger.getId().toString());
        }

        Integer currentUserId = oldPassenger.getUserId();

        // 检查邮箱是否已存在
        if (!Objects.equals(oldPassenger.getEmail(), passenger.getEmail()) && passengerRepository.existsByUserIdAndEmail(currentUserId, passenger.getEmail())) {
            throw new EntityExistException(Passenger.class, "email", passenger.getEmail());
        }

        // 检查手机号是否已存在
        if (!Objects.equals(oldPassenger.getPhone(), passenger.getPhone()) && passengerRepository.existsByUserIdAndPhone(currentUserId, passenger.getPhone())) {
            throw new EntityExistException(Passenger.class, "phone", passenger.getPhone());
        }

        // 设定当前用户
        passenger.setUserId(currentUserId);

        // 标记是否是注册用户
        User user = userRepository.findByEmail(passenger.getEmail());
        if (user == null) {
            user = userRepository.findByPhone(passenger.getPhone());
        }

        if (user != null) {
            passenger.setPassengerUserId(user.getUserId());
        }

        passengerRepository.save(passenger);
    }

    @Override
    public void deletePassenger(ObjectId id) {
        if (!passengerRepository.existsById(id)) {
            throw new EntityNotFoundException(Passenger.class, "id", id.toString());
        }
        passengerRepository.deleteById(id);
    }

    @Override
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }
}
