package com.example.flightapi.flight.service.impl;

import com.example.flightapi.flight.dto.FlightPageDTO;
import com.example.flightapi.flight.dto.FlightWithCabinsDTO;
import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.cabin.entity.CabinClass;
import com.example.flightapi.flight.repository.FlightRepository;
import com.example.flightapi.cabin.repository.CabinClassRepository;
import com.example.flightapi.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final CabinClassRepository cabinClassRepository;

    @Override
    public Flight getFlightById(String id) {
        return flightRepository.findById(id).orElse(null);
    }

    @Override
    public Flight updateFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight findFlightWithCabinClasses(String flightId) {
        return flightRepository.findById(flightId).orElse(null);
    }

    @Override
    public List<Flight> findFlightsWithCabinClasses(int departureAirportId, int destinationAirportId,
                                                    LocalDateTime startDate, LocalDateTime endDate) {
        // 如果没有提供日期范围，设置默认值
        LocalDateTime effectiveStartDate = startDate != null ? startDate : LocalDateTime.now();
        LocalDateTime effectiveEndDate = endDate != null ? endDate : effectiveStartDate.plusMonths(3);

        // 使用聚合查询获取航班及其舱位信息
        return flightRepository.findFlightsWithCabinClasses(
                departureAirportId,
                destinationAirportId,
                effectiveStartDate,
                effectiveEndDate);
    }

    @Override
    public List<Flight> findFlightsWithAvailableCabinClasses(int departureAirportId, int destinationAirportId,
                                                             LocalDateTime startDate, LocalDateTime endDate) {
        List<Flight> flights;
        if (startDate != null && endDate != null) {
            flights = flightRepository.findByDepartureAirportIdAndDestinationAirportIdAndDepartureTimeBetween(
                    departureAirportId, destinationAirportId, startDate, endDate);
        } else {
            flights = flightRepository.findByDepartureAirportIdAndDestinationAirportId(
                    departureAirportId, destinationAirportId);
        }

        return flights.stream()
                .filter(flight -> flight.getCabinClasses().stream()
                        .anyMatch(cabinClass -> cabinClass.getAvailableSeats() > 0))
                .collect(Collectors.toList());
    }

    @Override
    public CabinClass updateCabinClass(CabinClass cabinClass) {
        return cabinClassRepository.save(cabinClass);
    }

    @Override
    public Optional<CabinClass> getCabinClassByFlightIdAndType(String flightId, int classType) {
        return cabinClassRepository.findByFlightIdAndClassType(flightId, classType);
    }

    @Override
    public CabinClass updateCabinClassAvailability(String flightId, int classType, int seatCount) {
        CabinClass cabinClass = cabinClassRepository.findByFlightIdAndClassType(flightId, classType)
                .orElseThrow(() -> new RuntimeException("Cabin class not found"));
        cabinClass.setAvailableSeats(cabinClass.getAvailableSeats() + seatCount);
        return cabinClassRepository.save(cabinClass);
    }

    @Override
    public List<CabinClass> getCabinClassesByFlightId(String flightId) {
        return cabinClassRepository.findByFlightId(flightId);
    }

    @Override
    public FlightPageDTO getAllFlightsWithCabins(Pageable pageable) {
        Page<Flight> flightPage = flightRepository.findAll(pageable);

        List<FlightWithCabinsDTO> content = flightPage.getContent().stream()
                .map(flight -> {
                    FlightWithCabinsDTO dto = new FlightWithCabinsDTO();
                    dto.setFlight(flight);
                    dto.setCabins(cabinClassRepository.findByFlightId(flight.getId()));
                    return dto;
                })
                .collect(Collectors.toList());

        return FlightPageDTO.fromPage(new PageImpl<>(content, pageable, flightPage.getTotalElements()));
    }

    @Override
    public FlightPageDTO findFlightsWithCabinClassesByPage(int departureAirportId, int destinationAirportId,
                                                         LocalDateTime startDate, LocalDateTime endDate,
                                                         Pageable pageable) {
        // 如果没有提供日期范围，设置默认值
        LocalDateTime effectiveStartDate = startDate != null ? startDate : LocalDateTime.now();
        LocalDateTime effectiveEndDate = endDate != null ? endDate : effectiveStartDate.plusYears(1);

        // 计算分页参数
        int skip = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        // 使用聚合查询获取分页数据（已包含cabin_classes）
        List<Flight> flights = flightRepository.findFlightsWithCabinClassesPaged(
                departureAirportId,
                destinationAirportId,
                effectiveStartDate,
                effectiveEndDate,
                skip,
                limit);

        // 获取总记录数
        Long count = flightRepository.countFlightsWithCabinClasses(
                departureAirportId,
                destinationAirportId,
                effectiveStartDate,
                effectiveEndDate);

        // 如果count为null（没有匹配记录），设置为0
        long totalElements = count != null ? count : 0;

        // 将Flight对象转换为FlightWithCabinsDTO对象
        List<FlightWithCabinsDTO> content = flights.stream()
                .map(flight -> {
                    FlightWithCabinsDTO dto = new FlightWithCabinsDTO();
                    dto.setFlight(flight);
                    // 由于聚合查询已经包含了cabin_classes，我们可以直接使用它们
                    dto.setCabins(flight.getCabinClasses());
                    return dto;
                })
                .collect(Collectors.toList());

        // 构建并返回FlightPageDTO对象
        return FlightPageDTO.fromPage(new PageImpl<>(content, pageable, totalElements));
    }
}
