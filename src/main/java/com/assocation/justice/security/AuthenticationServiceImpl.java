package com.assocation.justice.security;

import com.assocation.justice.dto.*;
import com.assocation.justice.entity.User;
import com.assocation.justice.repository.UserRepository;
import com.assocation.justice.util.enumeration.Role;
import com.assocation.justice.util.enumeration.Source;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.client-id}")
    private String clientGoogleId;
    @Override
    public ResponseEntity<?> signup(SignUpRequest request) {
        // Check if the user already exists
        if (userRepository.findUserByUsername(request.getUsername()).isPresent()) {
            // Return an error response with a descriptive message
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("USER EXISTS");
        }

        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .username(request.getUsername()).password(passwordEncoder.encode(request.getPassword()))
                .regionResponsableId(request.getRegionResponsableId())
                .role(request.getRole() != null ? request.getRole() : Role.ADMIN).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var userDTO = this.mapUserToUserDto(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build());
    }

    @Override
    public ResponseEntity<?> signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findUserByUsername(request.getUsername()).orElse(null);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("USER NOT EXIST");
        }

        if (!user.isConfirmed() && !user.getRole().equals(Role.SUPER_ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("USER NOT CONFIRMED");
        }

        var userDTO = mapUserToUserDto(user);
        var jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build());
    }


    @Override
    public ResponseEntity<?> signinWithGoogle(SigninGoogleRequest request) {
        System.out.println("request = "+ request);
        GoogleIdToken.Payload payload = this.getGoogleData(request.getIdToken());
        User user = this.userRepository.findUserByEmail(payload.getEmail()).orElse(null);
        if(user != null) {
            user.setSource(Source.GOOGLE);
            var userDTO = mapUserToUserDto(user);
            var jwt = jwtService.generateToken(user);
            return ResponseEntity.ok(JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build());
        }
        return ResponseEntity.notFound().build();
    }

    public GoogleIdToken.Payload getGoogleData(String idToken) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientGoogleId))
                .build();

        GoogleIdToken googleIdToken = null;
        try {
            googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                System.out.println(googleIdToken.getPayload());
                return googleIdToken.getPayload();
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace(); // Logging the exception for debugging purposes
            // Handle the exception according to your application's requirements
        } finally {
            return googleIdToken.getPayload();
        }
    }

    public static void main(String[] args) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList("205380603904-0gq49n2s2i0pbk0u1j93j8vh6v2v74iu.apps.googleusercontent.com"))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify("eyJhbGciOiJSUzI1NiIsImtpZCI6ImU0YWRmYjQzNmI5ZTE5N2UyZTExMDZhZjJjODQyMjg0ZTQ5ODZhZmYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIyMDUzODA2MDM5MDQtMGdxNDluMnMyaTBwYmswdTFqOTNqOHZoNnYydjc0aXUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIyMDUzODA2MDM5MDQtMGdxNDluMnMyaTBwYmswdTFqOTNqOHZoNnYydjc0aXUuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTAxMzI0NDgwNjE1MzI2OTA3NTAiLCJlbWFpbCI6ImFsaWNoYWFiYW5lOThAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5iZiI6MTcwMjIxNDg4NSwibmFtZSI6IkFsaSBDaGFhYmFuZSIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NKX0Fwa05fZGl2MlA4RzlSXzhqWmNvUk5Rb1ZsOGJQV3hqREY4bmR0TnFNZmM9czk2LWMiLCJnaXZlbl9uYW1lIjoiQWxpIiwiZmFtaWx5X25hbWUiOiJDaGFhYmFuZSIsImxvY2FsZSI6ImZyIiwiaWF0IjoxNzAyMjE1MTg1LCJleHAiOjE3MDIyMTg3ODUsImp0aSI6ImViYjY3ODE2Njc1ZTEyZDE3NDQwNGZkZmMwMjRmYzc2NTFlMWU0MDIifQ.UB9OI-t00hMYU8OGqTb6eGfEJxZ468VxxcXo7sTMIh_CSjsQNu2BtApLUyq8JS9-DsoeTkNm2NrYuktnMXm-1FzDmXVS5KcK5E1irWt-ASFc2pZYUJOnBbL-ta2iAotvjkVUoHINlitUFT5jvY1VS-y1TkvK0NXJ2_FbuJAhH5f3RCvH6NwBaT29Bjl9kapkQCFHoEHXM-S39K9AuctqSHhDAY5d2O_8RStiO78hubOeH4ylmbvLmOuELADH3fA5SHrOBIGqRroxGpP75TEm4i6HCghpCk8zFzqjtvRs7n0IuXX-j4PtmIVFEodbr6PDrRcXeTyg2rchtNC-dvvBAA");
            if (googleIdToken != null) {
                System.out.println(googleIdToken.getPayload());
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace(); // Logging the exception for debugging purposes
            // Handle the exception according to your application's requirements
        }
        System.out.println("null");
    }


    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapUserToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.map(this::mapUserToUserDto).orElse(null);
    }
    @Override
    public UserDTO getUserByEmail(String email) {
        Optional<User> user = this.userRepository.findUserByEmail(email);
        return user.map(this::mapUserToUserDto).orElse(null);
    }

    @Override
    public ResponseEntity<UserDTO> changeUserState(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(IllegalArgumentException::new);
        user.setConfirmed(!user.isConfirmed());
        // Save the updated image back to the repository
        user = userRepository.save(user);
        logger.info("User = " + username + " change his status");
        // Return a ResponseEntity with the updated image and an HTTP status code
        return ResponseEntity.ok(mapUserToUserDto(user));
    }

    @Override
    public ResponseEntity<?> updateUser(SignUpRequest signUpRequest) {
        User user = userRepository.findById(signUpRequest.getId()).orElse(null);
        if(user != null) {
            user.setLastName(signUpRequest.getLastName());
            user.setFirstName(signUpRequest.getFirstName());
            if(signUpRequest.getEmail() != null) {
                user.setEmail(signUpRequest.getEmail());
            }
            if(!signUpRequest.getSource().equals(Source.LOGIN)) {
                user.setSource(signUpRequest.getSource());
            } else {
                user.setSource(Source.LOGIN);
            }
            if(signUpRequest.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            }
            // save the updated user
            user = userRepository.save(user);
            logger.info("User " + signUpRequest.getUsername() + " updated successfully");
            // Return a ResponseEntity with the updated image and an HTTP status code
            return ResponseEntity.ok(mapUserToUserDto(user));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return mapUserToUserDto(user);
        }
        return  null;
    }

    // Rest of your existing methods

    public UserDTO mapUserToUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setConfirmed(user.isConfirmed());
        if(user.getUsername()!= null) {
            userDTO.setUsername(user.getUsername());
        }
        if(user.getEmail()!= null) {
            userDTO.setEmail(user.getEmail());
        }
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLastName(user.getLastName());
        if(!user.getSource().equals(Source.LOGIN)) {
            userDTO.setSource(user.getSource());
        } else {
            userDTO.setSource(Source.LOGIN);
        }
        userDTO.setRegionResponsableId(user.getRegionResponsableId());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(passwordEncoder.encode(user.getPassword()));
        userDTO.setRole(user.getRole());

        return userDTO;
    }
}
