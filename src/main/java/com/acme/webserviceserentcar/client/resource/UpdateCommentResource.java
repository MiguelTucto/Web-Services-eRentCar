package com.acme.webserviceserentcar.client.resource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateCommentResource {
    @NotNull
    @NotBlank
    @Size(max = 8)
    private String date;

    @NotNull
    private int stars;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String comment;
}
