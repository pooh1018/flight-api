package com.example.flightapi.flight.service.impl;

import com.example.flightapi.common.utils.PageResult;
import com.example.flightapi.common.utils.PageUtil;
import com.example.flightapi.flight.dto.FlightWithCabinsDTO;
import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.cabin.entity.CabinClass;
import com.example.flightapi.flight.repository.FlightRepository;
import com.example.flightapi.cabin.repository.CabinClassRepository;
import com.example.flightapi.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final CabinClassRepository cabinClassRepository;

    /**
     * 确保Flight对象的cabinClasses属性已加载
     * @param flight Flight对象
     */
    private void ensureCabinClassesLoaded(Flight flight) {
        if (flight != null && (flight.getCabinClasses() == null || flight.getCabinClasses().isEmpty())) {
            flight.setCabinClasses(cabinClassRepository.findByFlightId(flight.getId()));
        }
    }

    /**
     * 将Flight对象转换为FlightWithCabinsDTO对象
     * @param flight Flight对象
     * @return FlightWithCabinsDTO对象
     */
    private FlightWithCabinsDTO convertToDTO(Flight flight) {
        if (flight == null) {
            return null;
        }

        // 确保舱位信息已加载
        ensureCabinClassesLoaded(flight);

        // 创建DTO并设置flight属性
        FlightWithCabinsDTO dto = new FlightWithCabinsDTO();
        dto.setFlight(flight);
        return dto;
    }

    /**
     * 将Flight列表转换为FlightWithCabinsDTO列表
     * @param flights Flight列表
     * @return FlightWithCabinsDTO列表
     */
    private List<FlightWithCabinsDTO> convertToDTOList(List<Flight> flights) {
        if (flights == null) {
            return Collections.emptyList();
        }

        return flights.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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
        Flight flight = flightRepository.findById(flightId).orElse(null);
        if (flight != null) {
            ensureCabinClassesLoaded(flight);
        }
        return flight;
    }

    @Override
    public List<Flight> findFlightsWithCabinClasses(int departureAirportId, int destinationAirportId,
                                                    LocalDateTime startDate, LocalDateTime endDate) {
        // 如果没有提供日期范围，设置默认值
        LocalDateTime effectiveStartDate = startDate != null ? startDate : LocalDateTime.now();
        LocalDateTime effectiveEndDate = endDate != null ? endDate : effectiveStartDate.plusMonths(3);

        // 使用聚合查询获取航班及其舱位信息
        List<Flight> flights = flightRepository.findFlightsWithCabinClasses(
                departureAirportId,
                destinationAirportId,
                effectiveStartDate,
                effectiveEndDate);

        // 确保所有航班都加载了舱位信息
        flights.forEach(this::ensureCabinClassesLoaded);

        return flights;
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

        // 确保所有航班都加载了舱位信息
        flights.forEach(this::ensureCabinClassesLoaded);

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
    public PageResult<FlightWithCabinsDTO> getAllFlightsWithCabins(Pageable pageable) {
        Page<Flight> flightPage = flightRepository.findAll(pageable);

        List<FlightWithCabinsDTO> content = convertToDTOList(flightPage.getContent());

        return PageUtil.toPage(
            content,
            pageable.getPageNumber() + 1, // Spring Data页码从0开始，转换为从1开始
            flightPage.getTotalPages(),
            flightPage.getTotalElements()
        );
    }

    @Override
    public PageResult<FlightWithCabinsDTO> findFlightsWithCabinClassesByPage(int departureAirportId, int destinationAirportId,
                                                         LocalDateTime startDate, LocalDateTime endDate,
                                                         Pageable pageable) {
        // 获取当前日期
        LocalDateTime now = LocalDateTime.now();

        // 如果没有提供日期范围，设置默认值
        LocalDateTime effectiveStartDate;
        if (startDate != null) {
            // 保留用户指定的完整时间，包括小时、分钟和秒
            effectiveStartDate = startDate;

            // 如果日期早于当前日期，则使用当前时间
            if (startDate.toLocalDate().isBefore(now.toLocalDate())) {
                effectiveStartDate = now;
            }
        } else {
            effectiveStartDate = now;
        }

        // 如果提供了endDate，保留其完整时间
        LocalDateTime effectiveEndDate;
        if (endDate != null) {
            effectiveEndDate = endDate.toLocalDate().atTime(23, 59, 59, 999_999_999);;
        } else {
            // 如果没有提供endDate，则使用startDate当天的最晚时间
            effectiveEndDate = effectiveStartDate.toLocalDate().atTime(23, 59, 59, 999_999_999);
        }

        // 计算分页参数
        int skip = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        // 将Spring Data的Sort转换为MongoDB的Document
        org.bson.Document sortDoc = new org.bson.Document();
        if (pageable.getSort() != null && pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                sortDoc.append(order.getProperty(), order.isAscending() ? 1 : -1);
            });
        }

        // 如果没有指定排序，默认按出发时间升序排序
        if (sortDoc.isEmpty()) {
            sortDoc.append("departure_time", 1);
        }

        // 使用聚合查询获取分页数据（已包含cabin_classes）
        List<Flight> flights = flightRepository.findFlightsWithCabinClassesPaged(
                departureAirportId,
                destinationAirportId,
                effectiveStartDate,
                effectiveEndDate,
                sortDoc,
                skip,
                limit);

        // 如果没有取到记录，就取startDate到2个星期以内的数据
        if (flights == null || flights.isEmpty()) {
            LocalDateTime twoWeeksLater = effectiveStartDate.plusWeeks(2);
            flights = flightRepository.findFlightsWithCabinClassesPaged(
                    departureAirportId,
                    destinationAirportId,
                    effectiveStartDate,
                    twoWeeksLater,
                    sortDoc,
                    skip,
                    limit);
        }

        // 将Flight对象转换为FlightWithCabinsDTO对象
        List<FlightWithCabinsDTO> content = convertToDTOList(flights);

        // 获取总记录数直接使用flights的个数
        long totalElements = content.size();

        // 由于我们只获取了当前页的数据，总页数可能不准确
        // 这里简单处理：如果当前页有数据且数量等于页大小，则认为可能有下一页
        int totalPages = content.isEmpty() ? 0 :
                         (content.size() < pageable.getPageSize() ? 1 : 2);

        // 使用PageUtil工具类创建PageResult对象
        return PageUtil.toPage(
            content,
            pageable.getPageNumber() + 1, // Spring Data页码从0开始，转换为从1开始
            totalPages,
            totalElements
        );
    }
}
