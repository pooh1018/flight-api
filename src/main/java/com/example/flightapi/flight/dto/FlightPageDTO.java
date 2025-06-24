package com.example.flightapi.flight.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class FlightPageDTO {
    private List<FlightWithCabinsDTO> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public static FlightPageDTO fromPage(Page<FlightWithCabinsDTO> page) {
        FlightPageDTO dto = new FlightPageDTO();
        dto.setContent(page.getContent());
        dto.setCurrentPage(page.getNumber());
        dto.setTotalPages(page.getTotalPages());
        dto.setTotalElements(page.getTotalElements());
        return dto;
    }
}
