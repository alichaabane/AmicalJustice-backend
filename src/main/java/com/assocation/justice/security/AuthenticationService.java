package com.assocation.justice.security;

import com.assocation.justice.dto.*;
import com.assocation.justice.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.List;

public interface AuthenticationService {
    ResponseEntity<?> signup(SignUpRequest request);

    ResponseEntity<?> signin(SigninRequest request);
    ResponseEntity<?> signinWithGoogle(SigninGoogleRequest request);

    ResponseEntity<UserDTO> changeUserState(String username);
    List<UserDTO> getAllUsers();
    UserDTO getCurrentUser(Authentication authentication);

    ResponseEntity<?> updateUser(SignUpRequest signUpRequest);
    UserDTO getUserById(Long id);
     UserDTO getUserByEmail(String email) ;
    void deleteUser(Long id);
}