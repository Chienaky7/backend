package com.example.myweb.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.myweb.repository.InvalidatedTokenRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenCleanupService {
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Scheduled(fixedRate = 86400000)
    public void cleanExpiredTokens() {
        int deletedCount = invalidatedTokenRepository.deleteAllByExpiryTimeBefore(new Date());
        log.info("Deleted {} expired tokens", deletedCount);
    }
}
