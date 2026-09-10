package com.api.personal.finance.infrastructure.persistence.mapper;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.AccountType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.infrastructure.persistence.entity.AccountJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    @Test
    void toDomain_ShouldMapAllFields() {
        UserJpaEntity userEntity = new UserJpaEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");

        AccountJpaEntity entity = AccountJpaEntity.builder()
                .id(10L)
                .name("Conta")
                .type(AccountType.CONTA_CORRENTE)
                .balance(BigDecimal.TEN)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .user(userEntity)
                .build();

        Account domain = AccountMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getUser().getId(), domain.getUser().getId());
    }

    @Test
    void toEntity_ShouldMapAllFields() {
        User user = User.builder().id(1L).name("John Doe").email("john.doe@example.com").build();
        Account domain = Account.builder()
                .id(10L)
                .name("Conta")
                .type(AccountType.CONTA_CORRENTE)
                .balance(BigDecimal.TEN)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .user(user)
                .build();

        UserJpaEntity userRef = new UserJpaEntity();
        userRef.setId(1L);

        AccountJpaEntity entity = AccountMapper.toEntity(domain, userRef);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getName(), entity.getName());
        assertEquals(userRef, entity.getUser());
    }

    @Test
    void toDomain_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(AccountMapper.toDomain(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDomainIsNull() {
        assertNull(AccountMapper.toEntity(null, new UserJpaEntity()));
    }
}