package com.assocation.justice.security;

import com.assocation.justice.dto.JwtAuthenticationResponse;
import com.assocation.justice.dto.SignUpRequest;
import com.assocation.justice.dto.SigninRequest;
import com.assocation.justice.dto.UserDTO;
import com.assocation.justice.entity.User;
import com.assocation.justice.repository.UserRepository;
import com.assocation.justice.util.enumeration.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
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
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRegionResponsableId(user.getRegionResponsableId());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(passwordEncoder.encode(user.getPassword()));
        userDTO.setRole(user.getRole());

        return userDTO;
    }
}