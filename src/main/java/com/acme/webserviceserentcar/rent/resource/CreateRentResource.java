package com.acme.webserviceserentcar.rent.resource;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class CreateRentResource {
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

    @NotNull
    private int rate;
}
