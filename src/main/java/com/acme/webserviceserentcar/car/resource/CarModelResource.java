package com.acme.webserviceserentcar.car.resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarModelResource {
    private Long id;
    private String name;
    private String imagePath;
    private Long carBrandId;
}
