package com.example.flightapi.booking.entity;

import lombok.Data;

@Data
public class Passenger {
    private String firstName;
    private String lastName;
    private String passportNumber;
    private String seatNumber;
}
