package com.assocation.justice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponse implements Serializable {
    private HttpStatus status;
    private String message;
    private JwtAuthenticationResponse jwtResponse; // Assuming JwtAuthenticationResponse is a DTO representing JWT authentication response

    // Constructor, getters, and setters
}
