package com.assocation.justice.resource;

import com.assocation.justice.dto.*;
import com.assocation.justice.entity.User;
import com.assocation.justice.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationResource {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest request) {
        return authenticationService.signin(request);
    }

    @PutMapping("/active/{username}")
    public ResponseEntity<UserDTO> toggleVisibleState(@PathVariable String username) {
        ResponseEntity<UserDTO> responseEntity = authenticationService.changeUserState(username);
        // Return a 404 response if changeImageVisibleState returns null
        return Objects.requireNonNullElseGet(responseEntity, () -> ResponseEntity.notFound().build()); // Return the response from changeImageVisibleState
    }
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = authenticationService.getAllUsers();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build(); // Or handle the case when no users are found
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody SignUpRequest request) {
        return authenticationService.updateUser(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authenticationService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current")
    public ResponseEntity<?> currentUserName(Authentication authentication) {
        UserDTO user = this.authenticationService.getCurrentUser(authentication);
            if(user != null) {
            return ResponseEntity.ok(user);
        } else {
            // Handle the case when the current user is not found or authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}