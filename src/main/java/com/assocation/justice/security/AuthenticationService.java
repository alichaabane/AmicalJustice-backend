package com.assocation.justice.security;

import com.assocation.justice.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AuthenticationService {
    ResponseEntity<?> signup(SignUpRequest request);

    ResponseEntity<?> signin(SigninRequest request);
    ResponseEntity<?> signinWithGoogle(SigninProviderRequest request);
    ResponseEntity<?> signinWithFacebook(SigninProviderRequest request);

    ResponseEntity<UserDTO> changeUserState(String username);
    List<UserDTO> getAllUsers();
    UserDTO getCurrentUser(Authentication authentication);

    ResponseEntity<?> updateUser(SignUpRequest signUpRequest);
    UserDTO getUserById(Long id);
     UserDTO getUserByEmail(String email) ;
    void deleteUser(Long id);
}
