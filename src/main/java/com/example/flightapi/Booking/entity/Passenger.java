package com.example.flightapi.Booking.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@Document(collection = "passenger")
public class Passenger {
    @Id
    private String id;

    @Field("booking_id")
    private String bookingId;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("seat_number")
    private String seatNumber;

    @DBRef(lazy = true)
    private Booking booking;
}
