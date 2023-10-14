package com.assocation.justice.security;

import com.assocation.justice.dto.JwtAuthenticationResponse;
import com.assocation.justice.dto.SignUpRequest;
import com.assocation.justice.dto.SigninRequest;
import com.assocation.justice.dto.UserDTO;
import com.assocation.justice.entity.User;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

}