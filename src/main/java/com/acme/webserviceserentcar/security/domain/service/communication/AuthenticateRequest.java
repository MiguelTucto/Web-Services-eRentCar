package com.acme.webserviceserentcar.security.domain.service.communication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateRequest {
    private String email;
    private String password;
}
