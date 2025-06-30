package com.example.flightapi.cabin.util;

import com.example.flightapi.cabin.dto.CabinClassDTO;
import com.example.flightapi.cabin.entity.CabinClass;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工具类，用于在CabinClass实体和DTO之间进行转换
 */
public class CabinClassMapper {

    /**
     * 将CabinClass实体转换为DTO
     * @param cabinClass 实体对象
     * @return DTO对象
     */
    public static CabinClassDTO toDTO(CabinClass cabinClass) {
        if (cabinClass == null) {
            return null;
        }

        CabinClassDTO dto = new CabinClassDTO();
        dto.setId(cabinClass.getId());
        dto.setFlightId(cabinClass.getFlightId().toString());
        dto.setClassType(cabinClass.getClassType());
        dto.setName(cabinClass.getName());
        dto.setAvailableSeats(cabinClass.getAvailableSeats());
        dto.setTotalSeats(cabinClass.getTotalSeats());
        dto.setPriceFactor(cabinClass.getPriceFactor());
        dto.setPrice(cabinClass.getPrice());

        return dto;
    }

    /**
     * 将DTO转换为CabinClass实体
     * @param dto DTO对象
     * @return 实体对象
     */
    public static CabinClass toEntity(CabinClassDTO dto) {
        if (dto == null) {
            return null;
        }

        CabinClass cabinClass = new CabinClass();
        cabinClass.setId(dto.getId());
        cabinClass.setFlightId(dto.getFlightId());
        cabinClass.setClassType(dto.getClassType());
        cabinClass.setName(dto.getName());
        cabinClass.setAvailableSeats(dto.getAvailableSeats());
        cabinClass.setTotalSeats(dto.getTotalSeats());
        cabinClass.setPriceFactor(dto.getPriceFactor());
        cabinClass.setPrice(dto.getPrice());

        return cabinClass;
    }

    /**
     * 将CabinClass实体列表转换为DTO列表
     * @param cabinClasses 实体列表
     * @return DTO列表
     */
    public static List<CabinClassDTO> toDTOList(List<CabinClass> cabinClasses) {
        if (cabinClasses == null) {
            return null;
        }

        return cabinClasses.stream()
                .map(CabinClassMapper::toDTO)
                .collect(Collectors.toList());
    }
}
