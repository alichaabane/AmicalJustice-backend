package com.assocation.justice.resource;

import com.assocation.justice.dto.SignUpRequest;
import com.assocation.justice.dto.SigninRequest;
import com.assocation.justice.dto.UserDTO;
import com.assocation.justice.entity.User;
import com.assocation.justice.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationResource {
    private final AuthenticationService authenticationService;
    private final OAuth2AuthorizedClientService clientService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest request) {
        return authenticationService.signin(request);
    }

    @GetMapping("/google/callback")
    public ResponseEntity<?> googleCallback(OAuth2AuthenticationToken authenticationToken) {
        try {
            if (authenticationToken != null) {
                OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                        authenticationToken.getAuthorizedClientRegistrationId(),
                        authenticationToken.getName()
                );
                // Process the client or perform further actions
                return ResponseEntity.ok("Successfully loaded authorized client");
            } else {
                return ResponseEntity.badRequest().body("Authentication token is null");
            }
        } catch (Exception e) {
            // Handle exceptions or errors appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/github/callback")
    @ResponseBody
    public ResponseEntity<?> githubCallback(OAuth2AuthenticationToken authenticationToken) {
        try {
            if (authenticationToken != null) {
                OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                        authenticationToken.getAuthorizedClientRegistrationId(),
                        authenticationToken.getName()
                );
                // Process the client or perform further actions
                return ResponseEntity.ok("Successfully loaded authorized client");
            } else {
                return ResponseEntity.badRequest().body("Authentication token is null");
            }
        } catch (Exception e) {
            // Handle exceptions or errors appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }        // Get email from OAuth2 user details
//        if (authentication != null && authentication.getPrincipal() instanceof UserDTO user1) {
//            String email = authentication.getPrincipal().toString();
//
//            // Check if email exists in the database
//            UserDTO user = authenticationService.getUserByEmail(email);
//
//            if (user != null) {
//                // Email exists in the database, perform actions accordingly
//                System.out.println(user);
//                return ResponseEntity.ok("User exists in the database");
//            } else {
//                // Email doesn't exist in the database, handle accordingly
//                return ResponseEntity.ok("User does not exist in the database");
//            }
//        }
//        return null;
//    }

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

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody SignUpRequest request) {
        return authenticationService.updateUser(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authenticationService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
       UserDTO userDTO = authenticationService.getUserById(id);
       if(userDTO != null) {
           return ResponseEntity.ok(userDTO);
       } else {
           return ResponseEntity.notFound().build(); // Or handle the case when no users are found
       }
    }

    @GetMapping("/current")
    public ResponseEntity<?> currentUserName(Authentication authentication) {
        UserDTO user = this.authenticationService.getCurrentUser(authentication);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            // Handle the case when the current user is not found or authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}