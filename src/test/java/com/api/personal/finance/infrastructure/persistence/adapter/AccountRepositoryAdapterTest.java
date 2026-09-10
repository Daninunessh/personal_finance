package com.api.personal.finance.infrastructure.persistence.adapter;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.AccountType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.infrastructure.persistence.entity.AccountJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import com.api.personal.finance.infrastructure.persistence.repository.AccountJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryAdapterTest {

    @Mock
    private AccountJpaRepository accountJpaRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AccountRepositoryAdapter adapter;

    @Test
    void findByUserId_ShouldReturnMappedDomainList() {
        UserJpaEntity userEntity = new UserJpaEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");

        AccountJpaEntity entity = AccountJpaEntity.builder()
                .id(10L)
                .name("Corrente")
                .type(AccountType.CONTA_CORRENTE)
                .balance(BigDecimal.TEN)
                .user(userEntity)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(accountJpaRepository.findByUserId(1L)).thenReturn(List.of(entity));

        List<Account> accounts = adapter.findByUserId(1L);

        assertEquals(1, accounts.size());
        assertEquals(10L, accounts.get(0).getId());
    }

    @Test
    void findByIdAndUserId_ShouldReturnOptionalMappedDomain() {
        UserJpaEntity userEntity = new UserJpaEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");

        AccountJpaEntity entity = AccountJpaEntity.builder()
                .id(10L)
                .name("Corrente")
                .type(AccountType.CONTA_CORRENTE)
                .balance(BigDecimal.TEN)
                .user(userEntity)
                .build();

        when(accountJpaRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(entity));

        Optional<Account> result = adapter.findByIdAndUserId(10L, 1L);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getId());
    }

    @Test
    void save_ShouldSaveAndReturnMappedDomain() {
        User user = User.builder().id(1L).name("John Doe").email("john.doe@example.com").build();
        Account domain = Account.builder()
                .id(10L)
                .name("Corrente")
                .type(AccountType.CONTA_CORRENTE)
                .balance(BigDecimal.TEN)
                .user(user)
                .build();

        UserJpaEntity userRef = new UserJpaEntity();
        userRef.setId(1L);
        userRef.setName("John Doe");
        userRef.setEmail("john.doe@example.com");

        AccountJpaEntity entityToSave = AccountJpaEntity.builder()
                .id(10L)
                .name("Corrente")
                .type(AccountType.CONTA_CORRENTE)
                .balance(BigDecimal.TEN)
                .user(userRef)
                .build();

        when(entityManager.getReference(eq(UserJpaEntity.class), eq(1L))).thenReturn(userRef);
        when(accountJpaRepository.save(any(AccountJpaEntity.class))).thenReturn(entityToSave);

        Account saved = adapter.save(domain);

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
        verify(entityManager).getReference(UserJpaEntity.class, 1L);
        verify(accountJpaRepository).save(any(AccountJpaEntity.class));
    }

    @Test
    void deleteById_ShouldCallJpaRepositoryDelete() {
        adapter.deleteById(10L);
        verify(accountJpaRepository).deleteById(10L);
    }
}