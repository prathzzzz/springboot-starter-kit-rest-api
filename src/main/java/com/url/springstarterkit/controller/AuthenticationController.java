package com.url.springstarterkit.controller;

import com.url.springstarterkit.dto.AuthenticationDTO;
import com.url.springstarterkit.dto.AuthenticationResponse;
import com.url.springstarterkit.dto.LogoutRequest;
import com.url.springstarterkit.dto.LogoutResponse;
import com.url.springstarterkit.dto.RegisterDTO;
import com.url.springstarterkit.exception.AuthenticationException;
import com.url.springstarterkit.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterDTO request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationDTO request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest request) {
        log.info("Processing logout request");
        LogoutResponse response = authenticationService.logout(request.getToken());
        return ResponseEntity.ok(response);
    }
} 