package com.acme.webserviceserentcar.security.domain.service.communication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private final String email;
    private final String token;
}
