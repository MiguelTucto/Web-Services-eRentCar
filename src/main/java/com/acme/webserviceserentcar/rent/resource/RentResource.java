package com.acme.webserviceserentcar.rent.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class RentResource {
    private Long id;
    private String startDate;
    private String finishDate;
    private int amount;
    private double rate;
    private Long clientId;
    private Long carId;
}
