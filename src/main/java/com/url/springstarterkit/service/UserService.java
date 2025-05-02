package com.url.springstarterkit.service;

import com.url.springstarterkit.dto.PaginationResponse;
import com.url.springstarterkit.dto.UserResponse;
import com.url.springstarterkit.exception.ResourceNotFoundException;
import com.url.springstarterkit.model.User;
import com.url.springstarterkit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND = "User not found with id: %d";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public PaginationResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String direction) {
        try {
            log.info("Fetching users with pagination - page: {}, size: {}, sortBy: {}, direction: {}", 
                    page, size, sortBy, direction);
            
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
            
            Page<User> userPage = userRepository.findAll(pageable);
            
            List<UserResponse> userResponses = userPage.getContent().stream()
                    .map(user -> modelMapper.map(user, UserResponse.class))
                    .collect(Collectors.toList());
            
            log.info("Successfully fetched {} users", userResponses.size());
            
            return new PaginationResponse<>(
                    userResponses,
                    userPage.getNumber(),
                    userPage.getSize(),
                    userPage.getTotalElements(),
                    userPage.getTotalPages(),
                    userPage.isLast()
            );
        } catch (Exception e) {
            log.error("Error fetching users: {}", e.getMessage(), e);
            throw e;
        }
    }

    public UserResponse getUserById(Long id) {
        try {
            log.info("Fetching user with id: {}", id);
            
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        String errorMessage = String.format(USER_NOT_FOUND, id);
                        log.error(errorMessage);
                        return new ResourceNotFoundException(errorMessage);
                    });
            
            log.info("Successfully fetched user with id: {}", id);
            return modelMapper.map(user, UserResponse.class);
        } catch (Exception e) {
            log.error("Error fetching user with id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
} 