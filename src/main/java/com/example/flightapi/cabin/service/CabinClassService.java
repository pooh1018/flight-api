package com.example.flightapi.cabin.service;

import com.example.flightapi.cabin.entity.CabinClass;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface CabinClassService {

    /**
     * 创建新的仓位
     * @param cabinClass 仓位信息
     * @return 创建的仓位
     */
    CabinClass createCabinClass(CabinClass cabinClass);

    /**
     * 更新仓位信息
     * @param cabinClass 仓位信息
     * @return 更新后的仓位
     */
    CabinClass updateCabinClass(CabinClass cabinClass);

    /**
     * 根据ID查询仓位
     * @param id 仓位ID
     * @return 仓位信息
     */
    Optional<CabinClass> getCabinClassById(String id);

    /**
     * 根据航班ID查询所有仓位
     * @param flightId 航班ID
     * @return 仓位列表
     */
    List<CabinClass> getCabinClassesByFlightId(String flightId);

    /**
     * 根据航班ID和仓位等级TYPE查询仓位
     * @param flightId 航班ID
     * @param classType 仓位等级TYPE
     * @return 仓位信息
     */
    Optional<CabinClass> getCabinClassByFlightIdAndclassType(String flightId, Integer classType);

    /**
     * 查询指定航班中可用座位数大于0的仓位
     * @param flightId 航班ID
     * @return 有可用座位的仓位列表
     */
    List<CabinClass> getAvailableCabinClassesByFlightId(String flightId);

    /**
     * 预订座位
     * @param cabinclassId 仓位ID
     * @param numSeats 预订座位数
     * @return 更新后的仓位信息
     * @throws IllegalStateException 如果可用座位不足
     */
    CabinClass bookSeats(String cabinclassId, int numSeats) throws IllegalStateException;

    /**
     * 取消座位预订
     * @param cabinclassId 仓位ID
     * @param numSeats 取消预订的座位数
     * @return 更新后的仓位信息
     * @throws IllegalStateException 如果取消座位数超过已预订数量
     */
    CabinClass cancelBooking(String cabinclassId, int numSeats) throws IllegalStateException;

    /**
     * 删除仓位
     * @param id 仓位ID
     */
    void deleteCabinClass(String id);

    /**
     * 减少仓位可用座位数
     * @param flightId 航班ID
     * @param classType 仓位等级TYPE
     * @param numSeats 减少的座位数
     * @return 更新后的仓位信息
     * @throws IllegalStateException 如果可用座位不足
     */
    CabinClass decreaseAvailableSeats(String flightId, Integer classType, int numSeats) throws IllegalStateException;

    /**
     * 增加仓位可用座位数
     * @param flightId 航班ID
     * @param classType 仓位等级TYPE
     * @param numSeats 增加的座位数
     * @return 更新后的仓位信息
     */
    CabinClass increaseAvailableSeats(String flightId, Integer classType, int numSeats);
}
