package com.example.flightapi.booking.dto;

import lombok.Data;

@Data
public class PassengerDTO {
    private String firstName;
    private String lastName;
    private String passportNumber;
    private String seatNumber;
}
