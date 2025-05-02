package com.url.springstarterkit.service;

import com.url.springstarterkit.model.BlacklistedToken;
import com.url.springstarterkit.repository.BlacklistedTokenRepository;
import com.url.springstarterkit.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtService jwtService;

    @Transactional
    public void blacklistToken(String token) {
        log.info("Blacklisting token");
        
        // Extract expiration date from token
        Date expirationDate = jwtService.extractExpiration(token);
        if (expirationDate == null) {
            log.warn("Could not extract expiration date from token");
            return;
        }

        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(expirationDate.toInstant());
        blacklistedToken.setCreatedAt(Instant.now());

        blacklistedTokenRepository.save(blacklistedToken);
        log.info("Token blacklisted successfully");
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }

    @Scheduled(fixedRate = 3600000) // Run every hour
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Cleaning up expired blacklisted tokens");
        blacklistedTokenRepository.deleteExpiredTokens(Instant.now());
        log.info("Expired blacklisted tokens cleanup completed");
    }
} 