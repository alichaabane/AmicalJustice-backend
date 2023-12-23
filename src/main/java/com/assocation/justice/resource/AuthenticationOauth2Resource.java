package com.assocation.justice.resource;

import com.assocation.justice.dto.SigninProviderRequest;
import com.assocation.justice.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class AuthenticationOauth2Resource {
    private final AuthenticationService authenticationService;

    @PostMapping("/signin-with-google")
    public ResponseEntity<?> signInWithGoogle(@RequestBody SigninProviderRequest request) {
        return authenticationService.signinWithGoogle(request);
    }

    @PostMapping("/signin-with-facebook")
    public ResponseEntity<?> signInWithFacebook(@RequestBody SigninProviderRequest request) {
        return authenticationService.signinWithFacebook(request);
    }

}
