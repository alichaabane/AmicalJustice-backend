package com.assocation.justice.security;

import com.assocation.justice.dto.*;
import com.assocation.justice.entity.RegionResponsable;
import com.assocation.justice.entity.User;
import com.assocation.justice.repository.UserRepository;
import com.assocation.justice.util.enumeration.Role;
import com.assocation.justice.util.enumeration.Source;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService, Serializable {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.client-id}")
    private String clientGoogleId;
    @Override
    @Cacheable(cacheNames = "justiceCache", key = "'signup:' + #request.getUsername()")
    public ResponseEntity<?> signup(SignUpRequest request) {
        // Check if the user already exists
        if (userRepository.findUserByUsername(request.getUsername()).isPresent()) {
            // Return an error response with a descriptive message
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("USER EXISTS");
        }

        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .confirmed(request.isConfirmed())
                .username(request.getUsername())
                .source(request.getSource() != null ? request.getSource() : Source.LOGIN)
                .password(passwordEncoder.encode(request.getPassword()))
                .regionResponsableId(request.getRegionResponsableId())
                .role(request.getRole() != null ? request.getRole() : Role.ADMIN).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var userDTO = this.mapUserToUserDto(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build());
    }

    @Override
    @Cacheable(cacheNames = "justiceCache", key = "'signin:' + #request.getUsername()")
    public SigninResponse signin(SigninRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var user = userRepository.findUserByUsername(request.getUsername()).orElse(null);

            if (user == null) {
                return new SigninResponse(HttpStatus.NOT_FOUND, "USER NOT EXIST", null);
            }

            if (!user.isConfirmed() && !user.getRole().equals(Role.SUPER_ADMIN)) {
                return new SigninResponse(HttpStatus.FORBIDDEN, "USER NOT CONFIRMED", null);
            }

            var userDTO = mapUserToUserDto(user);
            var jwt = jwtService.generateToken(user);
            return new SigninResponse(HttpStatus.OK, "SUCCESS", JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build());
        } catch (AuthenticationException e) {
            return new SigninResponse(HttpStatus.UNAUTHORIZED, "INVALID CREDENTIALS", null);
        }
    }


    @Override
    @Cacheable(cacheNames = "justiceCache", key = "'signinWithGoogle:' + #request.getToken()")
    public ResponseEntity<?> signinWithGoogle(SigninProviderRequest request) {
        System.out.println("request GOOGLE login = "+ request);
        GoogleIdToken.Payload payload = this.getGoogleData(request.getToken());
        User user = this.userRepository.findUserByEmail(payload.getEmail()).orElse(null);
        if(user != null) {
            user.setSource(Source.GOOGLE);
            var userDTO = mapUserToUserDto(user);
            var jwt = jwtService.generateToken(user);
            return ResponseEntity.ok(JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @Cacheable(cacheNames = "justiceCache", key = "'signinWithFacebook:' + #request.getToken()")
    public ResponseEntity<?> signinWithFacebook(SigninProviderRequest request) {
        System.out.println("request facebook login = "+ request);
        org.springframework.social.facebook.api.User user = this.getFacebookData(request.getToken());
        User userJustice = this.userRepository.findUserByEmail(user.getEmail()).orElse(null);
        if(userJustice != null) {
            userJustice.setSource(Source.FACEBOOK);
            var userDTO = mapUserToUserDto(userJustice);
            var jwt = jwtService.generateToken(userJustice);
            return ResponseEntity.ok(JwtAuthenticationResponse.builder().token(jwt).user(userDTO).build());
        }
        return ResponseEntity.notFound().build();
    }

    public GoogleIdToken.Payload getGoogleData(String idToken) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

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

    public org.springframework.social.facebook.api.User getFacebookData(String token) {
        Facebook facebook = new FacebookTemplate(token);
        final String[] fields = { "email", "name", "first_name", "last_name"};
        return facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
    }

    @Override
    @Cacheable(cacheNames = "justiceCache", key = "'allUsers'")
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapUserToUserDto).collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = "justiceCache",key = "#pageRequest")
    public PageRequestData<UserDTO> getAllUsersPaginated(PageRequest pageRequest) {
        Page<User> userPage = userRepository.findAll(pageRequest);
        PageRequestData<UserDTO> customPageResponse = new PageRequestData<>();
        customPageResponse.setContent(userPage.map(this::mapUserToUserDto).getContent());
        customPageResponse.setTotalPages(userPage.getTotalPages());
        customPageResponse.setTotalElements(userPage.getTotalElements());
        customPageResponse.setNumber(userPage.getNumber());
        customPageResponse.setSize(userPage.getSize());
        logger.info("Fetching All users of Page N° " + pageRequest.getPageNumber());
        return customPageResponse;
    }

    @Override
    @Cacheable(cacheNames = "justiceCache",key = "#id")
    public UserDTO getUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.map(this::mapUserToUserDto).orElse(null);
    }
    @Override
    @Cacheable(cacheNames = "justiceCache",key = "#email")
    public UserDTO getUserByEmail(String email) {
        Optional<User> user = this.userRepository.findUserByEmail(email);
        return user.map(this::mapUserToUserDto).orElse(null);
    }

    @Override
    @Cacheable(cacheNames = "justiceCache",key = "#username")
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
    @Cacheable(cacheNames = "justiceCache",key = "#signUpRequest.getId()")
    public ResponseEntity<?> updateUser(SignUpRequest signUpRequest) {
        User user = userRepository.findById(signUpRequest.getId()).orElse(null);
        if(user != null) {
            user.setLastName(signUpRequest.getLastName());
            user.setFirstName(signUpRequest.getFirstName());
            if(signUpRequest.getEmail() != null) {
                user.setEmail(signUpRequest.getEmail());
            }
            if(signUpRequest.getSource() != null && !signUpRequest.getSource().equals(Source.LOGIN)) {
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
    @Cacheable(cacheNames = "justiceCache",key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "justiceCache",key = "#authentication.getPrincipal()")
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
//        if(!user.getSource().equals(Source.LOGIN)) {
//            userDTO.setSource(user.getSource());
//        } else {
//            userDTO.setSource(Source.LOGIN);
//        }
        userDTO.setSource(user.getSource());

        userDTO.setRegionResponsableId(user.getRegionResponsableId());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(passwordEncoder.encode(user.getPassword()));
        userDTO.setRole(user.getRole());

        return userDTO;
    }
}
