package com.api.personal.finance.infrastructure.persistence.mapper;

import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    @DisplayName("Deve converter UserJpaEntity para User (Domain)")
    void shouldMapEntityToDomain() {
        Instant now = Instant.now();
        UserJpaEntity entity = UserJpaEntity.builder()
                .id(1L)
                .name("João")
                .email("joao@email.com")
                .password("encodedPass")
                .createdAt(now)
                .updatedAt(now)
                .build();

        User domain = UserMapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getName()).isEqualTo("João");
        assertThat(domain.getEmail()).isEqualTo("joao@email.com");
        assertThat(domain.getPassword()).isEqualTo("encodedPass");
        assertThat(domain.getCreatedAt()).isEqualTo(now);
        assertThat(domain.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter UserJpaEntity nulo")
    void shouldReturnNullWhenEntityIsNull() {
        assertThat(UserMapper.toDomain(null)).isNull();
    }

    @Test
    @DisplayName("Deve converter User (Domain) para UserJpaEntity")
    void shouldMapDomainToEntity() {
        Instant now = Instant.now();
        User domain = new User(1L, "João", "joao@email.com", "encodedPass", now, now);

        UserJpaEntity entity = UserMapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("João");
        assertThat(entity.getEmail()).isEqualTo("joao@email.com");
        assertThat(entity.getPassword()).isEqualTo("encodedPass");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter User nulo")
    void shouldReturnNullWhenDomainIsNull() {
        assertThat(UserMapper.toEntity(null)).isNull();
    }
}