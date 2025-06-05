package com.example.flightapi.Booking.dto;

import lombok.Data;

@Data
public class PassengerDTO {
    private String id;
    private String bookingId;
    private String firstName;
    private String lastName;
    private String seatNumber;
}
