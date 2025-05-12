package com.url.springstarterkit.service;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.url.springstarterkit.config.JacksonConfig;
import com.url.springstarterkit.dto.AuthenticationDTO;
import com.url.springstarterkit.dto.AuthenticationResponse;
import com.url.springstarterkit.dto.LogoutResponse;
import com.url.springstarterkit.dto.RegisterDTO;
import com.url.springstarterkit.exception.AuthenticationException;
import com.url.springstarterkit.exception.DuplicateEmailException;
import com.url.springstarterkit.exception.InvalidRequestException;
import com.url.springstarterkit.exception.ResourceNotFoundException;
import com.url.springstarterkit.model.Role;
import com.url.springstarterkit.model.User;
import com.url.springstarterkit.repository.RoleRepository;
import com.url.springstarterkit.repository.UserRepository;
import com.url.springstarterkit.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String DEFAULT_ROLE_NOT_FOUND = "Default role USER not found";
    private static final String ROLE_NOT_FOUND = "Role %s not found";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String INVALID_CREDENTIALS = "Invalid credentials";
    private static final String EMAIL_ALREADY_EXISTS = "Email %s is already registered";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthenticationResponse register(RegisterDTO request) {
    
        log.info("Attempting to register new user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - Email already exists: {}", request.getEmail());
            throw new DuplicateEmailException(String.format(EMAIL_ALREADY_EXISTS, request.getEmail()));
        }

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> {
                    log.error("Default USER role not found in database");
                    return new ResourceNotFoundException(DEFAULT_ROLE_NOT_FOUND);
                }));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        
        log.info("Saving new user to database");
        userRepository.save(user);
        log.info("User successfully registered with ID: {}", user.getId());

        String jwtToken = jwtService.generateToken(user);
        log.info("JWT token generated for user: {}", user.getEmail());
        
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationDTO request) {
        log.info("Attempting to authenticate user: {}", request.getEmail());
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            log.info("Authentication successful for user: {}", request.getEmail());
        } catch (Exception e) {
            log.error("Authentication failed for user {}: {}", request.getEmail(), e.getMessage());
            throw new AuthenticationException(INVALID_CREDENTIALS);
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found after successful authentication: {}", request.getEmail());
                    return new ResourceNotFoundException(USER_NOT_FOUND);
                });
        
        String jwtToken = jwtService.generateToken(user);
        log.info("JWT token generated for authenticated user: {}", user.getEmail());
        
        return new AuthenticationResponse(jwtToken);
    }

    public LogoutResponse logout(String token) {
        log.info("Processing logout request");
        
        if (token == null || token.isBlank()) {
            log.warn("Invalid token provided for logout");
            return new LogoutResponse("Invalid token", false);
        }

        try {
            tokenBlacklistService.blacklistToken(token);
            SecurityContextHolder.clearContext();
            log.info("User logged out successfully");
            return new LogoutResponse("Logout successful", true);
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            return new LogoutResponse("Logout failed: " + e.getMessage(), false);
        }
    }
} 