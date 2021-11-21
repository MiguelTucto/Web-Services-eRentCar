package com.acme.webserviceserentcar.car.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class CarResource {
    private Long id;
    private String address;
    private String brand;
    private int year;
    private String model;
    private int mileage;
    private int seating;
    private boolean manual;
    private int carValueInDollars;
    private String extraInformation;
    private int rate;
    private int rentAmountDay;
    private Long client;
}