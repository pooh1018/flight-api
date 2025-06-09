package com.example.flightapi.Booking.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDTO {
    private String id;
    private int userId;
    private String flightId;
    private List<PassengerDTO> passengers;
    private Double totalPrice;
    private String status;
    private LocalDateTime bookingTime;
    private String paymentMethod;
    private String contactEmail;
    private String contactPhone;
}

