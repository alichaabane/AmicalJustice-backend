package com.assocation.justice.security;

import com.assocation.justice.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    ResponseEntity<UserDTO> changeUserState(String username);
    List<UserDTO> getAllUsers();
}