package com.url.springstarterkit.controller;

import com.url.springstarterkit.dto.ApiResponse;
import com.url.springstarterkit.dto.PaginationResponse;
import com.url.springstarterkit.dto.UserResponse;
import com.url.springstarterkit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        try {
            log.info("Received request to get all users with pagination");
            PaginationResponse<UserResponse> response = userService.getAllUsers(page, size, sortBy, direction);
            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
        } catch (Exception e) {
            log.error("Error in getAllUsers: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        try {
            log.info("Received request to get user with id: {}", id);
            UserResponse response = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", response));
        } catch (Exception e) {
            log.error("Error in getUserById: {}", e.getMessage(), e);
            throw e;
        }
    }
} 