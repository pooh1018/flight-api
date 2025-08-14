package com.example.flightapi.flight.service;

import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.cabin.entity.CabinClass;

import com.example.flightapi.common.utils.PageResult;
import com.example.flightapi.flight.dto.FlightWithCabinsDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

public interface FlightService {

    /**
     * 根据ID获取航班信息
     * @param id 航班ID
     * @return 航班信息
     */
    Flight getFlightById(String id);

    /**
     * 更新航班信息
     * @param flight 要更新的航班信息
     * @return 更新后的航班信息
     */
    Flight updateFlight(Flight flight);

    /**
     * 根据ID获取航班及其关联的仓位信息
     * @param flightId 航班ID
     * @return 航班信息
     */
    Flight findFlightWithCabinClasses(String flightId);

    /**
     * 查询航班及其关联的仓位信息
     * @param departureAirportId 出发城市
     * @param destinationAirportId 到达城市
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 航班列表
     */
    List<Flight> findFlightsWithCabinClasses(int departureAirportId, int destinationAirportId,
                                           LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询航班及其关联的可用仓位信息
     * @param departureAirportId 出发城市
     * @param destinationAirportId 到达城市
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 航班列表
     */
    List<Flight> findFlightsWithAvailableCabinClasses(int departureAirportId, int destinationAirportId,
                                                    LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 更新航班的舱位信息
     * @param cabinClass 舱位信息
     * @return 更新后的舱位信息
     */
    CabinClass updateCabinClass(CabinClass cabinClass);

    /**
     * 根据航班ID和舱位类型获取舱位信息
     * @param flightId 航班ID
     * @param classType 舱位类型
     * @return 舱位信息
     */
    Optional<CabinClass> getCabinClassByFlightIdAndType(String flightId, int classType);

    /**
     * 更新舱位可用性（增加或减少可用座位数）
     * @param flightId 航班ID
     * @param classType 舱位类型
     * @param seatCount 座位数量（正数表示增加，负数表示减少）
     * @return 更新后的舱位信息
     */
    CabinClass updateCabinClassAvailability(String flightId, int classType, int seatCount);

    /**
     * 获取航班的所有舱位信息
     * @param flightId 航班ID
     * @return 舱位信息列表
     */
    List<CabinClass> getCabinClassesByFlightId(String flightId);

    PageResult<FlightWithCabinsDTO> getAllFlightsWithCabins(Pageable pageable);

    /**
     * 分页查询：根据出发机场ID、到达机场ID和时间范围查询航班
     * @param departureAirportId 出发机场ID
     * @param destinationAirportId 到达机场ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageable 分页参数
     * @return 分页航班数据
     */
    PageResult<FlightWithCabinsDTO> findFlightsWithCabinClassesByPage(int departureAirportId, int destinationAirportId,
                                                  LocalDateTime startDate, LocalDateTime endDate,
                                                  Pageable pageable);
}
