package org.company.insurance.service;

import lombok.RequiredArgsConstructor;
import org.company.insurance.dto.*;
import org.company.insurance.entity.User;
import org.company.insurance.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.company.insurance.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);



    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        logger.info("Sign up request received for username: {}", request.getUsername());

        var user = org.company.insurance.entity.User.builder()
                .username(request.getUsername())
                //.email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        UserCreationDto userDto = userMapper.toCrDto(user);
        logger.info("Creating new user: {}", request.getUsername());
        userService.createUser(userDto);

        var jwt = jwtService.generateToken(user);
        logger.info("User created and JWT token generated for username: {}", request.getUsername());

        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        logger.info("Sign in request received for username: {}", request.getUsername());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        logger.info("User authenticated and JWT token generated for username: {}", request.getUsername());

        return new JwtAuthenticationResponse(jwt);
    }
}
