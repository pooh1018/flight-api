package com.example.flightapi.cabin.repository;

import com.example.flightapi.cabin.entity.CabinClass;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CabinClassRepository extends MongoRepository<CabinClass, String> {
    List<CabinClass> findByFlightId(String flightId);
    void deleteByFlightId(String flightId);

    /**
     * 根据航班ID和仓位等级TYPE查询仓位
     */
    Optional<CabinClass> findByFlightIdAndClassType(String flightId, Integer classType);

    /**
     * 查询指定航班中可用座位数大于0的仓位
     */
    @Query("{'flightId': ?0, 'availableSeats': {$gt: 0}}")
    List<CabinClass> findAvailableCabinsByFlightId(String flightId);
}
