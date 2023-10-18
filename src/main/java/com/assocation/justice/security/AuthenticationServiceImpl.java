package com.assocation.justice.security;

import com.assocation.justice.dto.JwtAuthenticationResponse;
import com.assocation.justice.dto.SignUpRequest;
import com.assocation.justice.dto.SigninRequest;
import com.assocation.justice.dto.UserDTO;
import com.assocation.justice.entity.User;
import com.assocation.justice.repository.UserRepository;
import com.assocation.justice.util.enumeration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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