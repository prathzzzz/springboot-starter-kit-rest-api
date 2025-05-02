package com.url.springstarterkit.repository;

import com.url.springstarterkit.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    Optional<BlacklistedToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM BlacklistedToken bt WHERE bt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") Instant now);
} 