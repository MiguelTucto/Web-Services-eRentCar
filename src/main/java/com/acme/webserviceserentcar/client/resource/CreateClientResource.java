package com.acme.webserviceserentcar.client.resource;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class CreateClientResource {
    @Size(max = 30)
    private String names;

    @Size(max = 30)
    private String lastNames;

    @Size(max = 50)
    private String address;

    private Long cellphoneNumber;

    private int averageResponsibility;

    private int responseTime;

    private double rate;

    private Long userId;
}
