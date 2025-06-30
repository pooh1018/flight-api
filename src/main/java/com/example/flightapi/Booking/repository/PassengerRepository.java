package com.example.flightapi.Booking.repository;

import com.example.flightapi.Booking.entity.Passenger;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository extends MongoRepository<Passenger, ObjectId> {
    Optional<Passenger> findById(String id);

    List<Passenger> findByUserId(int userId);

    List<Passenger> findAllByIdIn(List<String> ids);
    
    boolean existsByUserIdAndEmail(Integer userId, String email);
    
    boolean existsByUserIdAndPhone(Integer userId, String phone);
}
