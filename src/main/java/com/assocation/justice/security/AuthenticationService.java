package com.assocation.justice.security;

import com.assocation.justice.dto.JwtAuthenticationResponse;
import com.assocation.justice.dto.SignUpRequest;
import com.assocation.justice.dto.SigninRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}