package com.assocation.justice.resource;

import com.assocation.justice.dto.JwtAuthenticationResponse;
import com.assocation.justice.dto.SignUpRequest;
import com.assocation.justice.dto.SigninRequest;
import com.assocation.justice.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationResource {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.signin(request);
        if(jwtAuthenticationResponse != null) {
            return ResponseEntity.ok(jwtAuthenticationResponse);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}