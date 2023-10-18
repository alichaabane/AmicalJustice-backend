package com.assocation.justice.security;

import com.assocation.justice.dto.*;
import com.assocation.justice.entity.Image;
import com.assocation.justice.entity.User;
import com.assocation.justice.repository.UserRepository;
import com.assocation.justice.service.impl.ImageServiceImpl;
import com.assocation.justice.util.enumeration.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .username(request.getUsername()).password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.ADMIN).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var userDTO = this.mapUserToUserDto(user);
        return JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        var userDTO = userRepository.findUserByUsername(request.getUsername()).map(this::mapUserToUserDto)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (user.isConfirmed() || user.getRole().name().equals("SUPER_ADMIN")) {
            var jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build();
        } else {
            return null;
        }
    }
    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapUserToUserDto).collect(Collectors.toList());
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

    // Rest of your existing methods

    public UserDTO mapUserToUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setConfirmed(user.isConfirmed());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());

        return userDTO;
    }
}