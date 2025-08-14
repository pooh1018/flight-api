package com.example.flightapi.flight.repository;

import com.example.flightapi.flight.entity.Flight;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface FlightRepository extends MongoRepository<Flight, String> {

    // 新增的聚合查询方法，获取航班及其关联的仓位信息
    @Aggregation(pipeline = {
        "{ $match: { _id: ?0 } }",
        "{ $lookup: { " +
            "from: 'cabin_class', " +
            "localField: '_id', " +
            "foreignField: 'flight_id', " +
            "as: 'cabin_classes' " +
        "} }"
    })
    Flight findFlightWithCabinClasses(String flightId);

    // 查询多个航班及其关联的仓位信息
    @Aggregation(pipeline = {
        "{ $match: { $and: [ " +
            "{ 'departure_airport_id': ?0 }, " +
            "{ 'destination_airport_id': ?1 }, " +
            "{ 'departure_time': { $gte: ?2 } }, " +
            "{ 'arrival_time': { $lte: ?3 } } " +
        "] } }",
        "{ $lookup: { " +
            "from: 'cabin_class', " +
            "localField: '_id', " +
            "foreignField: 'flight_id', " +
            "as: 'cabin_classes' " +
        "} }"
    })
    List<Flight> findFlightsWithCabinClasses(int departureAirportId, int destinationAirportId,
                                             LocalDateTime startDate, LocalDateTime endDate);

    // 分页查询所有航班
    Page<Flight> findAll(Pageable pageable);

    // 根据出发城市、到达城市和时间范围查询航班
    List<Flight> findByDepartureAirportIdAndDestinationAirportIdAndDepartureTimeBetween(
            int departureAirportId,
            int destinationAirportId,
        LocalDateTime departureTimeStart,
        LocalDateTime departureTimeEnd);

    // 根据出发城市、到达城市查询航班（不限制日期）
    @Query("{'departure_airport_id': ?0, 'destination_airport_id': ?1}")
    List<Flight> findByDepartureAirportIdAndDestinationAirportId(int departureAirportId, int destinationAirportId);

    // 分页查询：根据出发机场ID、到达机场ID和时间范围查询航班
    Page<Flight> findByDepartureAirportIdAndDestinationAirportIdAndDepartureTimeBetween(
            int departureAirportId,
            int destinationAirportId,
        LocalDateTime departureTimeStart,
        LocalDateTime departureTimeEnd,
        Pageable pageable);

    // 分页查询：根据出发机场ID、到达机场ID查询航班（不限制日期）
    @Query("{'departure_airport_id': ?0, 'destination_airport_id': ?1}")
    Page<Flight> findByDepartureAirportIdAndDestinationAirportId(
            int departureAirportId,
            int destinationAirportId,
        Pageable pageable);

    // 根据日期范围查询航班
    List<Flight> findByDepartureTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 分页版本的聚合查询，用于获取航班及其关联的仓位信息
    @Aggregation(pipeline = {
        "{ $match: { $and: [ " +
            "{ 'departure_airport_id': ?0 }, " +
            "{ 'destination_airport_id': ?1 }, " +
            "{ 'departure_time': { $gte: ?2, $lte: ?3 } } " +
        "] } }",
        "{ $lookup: { " +
            "from: 'cabin_class', " +
            "localField: '_id', " +
            "foreignField: 'flight_id', " +
            "as: 'cabin_classes' " +
        "} }",
        "{ $sort: ?4 }",
        "{ $skip: ?5 }",
        "{ $limit: ?6 }"
    })
    List<Flight> findFlightsWithCabinClassesPaged(
            int departureAirportId,
            int destinationAirportId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        org.bson.Document sort,
        int skip,
        int limit
    );

    // 获取符合条件的航班总数
    @Aggregation(pipeline = {
        "{ $match: { $and: [ " +
            "{ 'departure_airport_id': ?0 }, " +
            "{ 'destination_airport_id': ?1 }, " +
            "{ 'departure_time': { $gte: ?2, $lte: ?3 } } " +
        "] } }",
        "{ $count: 'total' }"
    })
    Long countFlightsWithCabinClasses(
            int departureAirportId,
            int destinationAirportId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
}
