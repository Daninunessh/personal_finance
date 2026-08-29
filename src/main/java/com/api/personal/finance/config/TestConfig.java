package com.api.personal.finance.config;

import com.api.personal.finance.infrastructure.persistence.UserJpaEntity;
import com.api.personal.finance.infrastructure.persistence.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestConfig implements CommandLineRunner {

    private final UserJpaRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        UserJpaEntity user1 = UserJpaEntity.builder()
                .id(null)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .createdAt(LocalDateTime.of(2026, 7, 25, 20, 0))
                .updatedAt(LocalDateTime.now())
                .build();

        UserJpaEntity user2 = UserJpaEntity.builder()
                .id(null)
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .password("password456")
                .createdAt(LocalDateTime.of(2026, 7, 25, 20, 0))
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.saveAll(Arrays.asList(user1, user2));

    }
}
