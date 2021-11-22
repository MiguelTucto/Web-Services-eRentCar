package com.acme.webserviceserentcar.car.resource;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class CreateCarBrandResource {
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String name;
}
