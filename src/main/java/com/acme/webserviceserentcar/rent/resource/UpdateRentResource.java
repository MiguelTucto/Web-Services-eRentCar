package com.acme.webserviceserentcar.rent.resource;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateRentResource {
    @NotNull
    @NotBlank
    @Size(max = 15)
    private String startDate;

    @NotNull
    @NotBlank
    @Size(max = 15)
    private String finishDate;

    @NotNull
    private int amount;
}
