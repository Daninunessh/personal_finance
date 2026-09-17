package com.api.personal.finance.infrastructure.persistence.adapter;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.AccountType;
import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.entity.CategoryType;
import com.api.personal.finance.domain.entity.Transaction;
import com.api.personal.finance.infrastructure.persistence.entity.AccountJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.CategoryJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.TransactionJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import com.api.personal.finance.infrastructure.persistence.repository.TransactionJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryAdapterTest {

    @Mock
    private TransactionJpaRepository transactionJpaRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TransactionRepositoryAdapter transactionRepositoryAdapter;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transactionRepositoryAdapter, "entityManager", entityManager);
    }

    @Test
    void shouldReturnTransactionsByUserIdMappedToDomain() {

        TransactionJpaEntity entity = createValidTransactionJpaEntity();

        when(transactionJpaRepository.findByUserId(1L)).thenReturn(List.of(entity));
        List<Transaction> result = transactionRepositoryAdapter.findByUserId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());

        Transaction transaction = result.get(0);
        assertEquals("Compra", transaction.getDescription());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals(LocalDate.of(2026, 9, 15), transaction.getTransactionDate());
        verify(transactionJpaRepository).findByUserId(1L);
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoTransactions() {

        when(transactionJpaRepository.findByUserId(1L)).thenReturn(List.of());
        List<Transaction> result = transactionRepositoryAdapter.findByUserId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionJpaRepository).findByUserId(1L);
    }

    @Test
    void shouldReturnTransactionsByUserIdAndDateBetween() {

        LocalDate startDate = LocalDate.of(2026, 9, 1);
        LocalDate endDate = LocalDate.of(2026, 9, 30);
        TransactionJpaEntity entity = createValidTransactionJpaEntity();

        when(transactionJpaRepository.findByUserIdAndTransactionDateBetween(1L, startDate, endDate)).thenReturn(List.of(entity));
        List<Transaction> result = transactionRepositoryAdapter.findByUserIdAndTransactionDateBetween(1L, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());

        Transaction transaction = result.get(0);

        assertEquals("Compra", transaction.getDescription());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals(LocalDate.of(2026, 9, 15), transaction.getTransactionDate());
        verify(transactionJpaRepository).findByUserIdAndTransactionDateBetween(1L, startDate, endDate);
    }

    @Test
    void shouldReturnTransactionByIdAndUserId() {

        TransactionJpaEntity entity = createValidTransactionJpaEntity();
        when(transactionJpaRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(entity));

        Optional<Transaction> result = transactionRepositoryAdapter.findByIdAndUserId(10L, 1L);

        assertTrue(result.isPresent());
        Transaction transaction = result.get();

        assertEquals("Compra", transaction.getDescription());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertEquals(LocalDate.of(2026, 9, 15), transaction.getTransactionDate());
        verify(transactionJpaRepository).findByIdAndUserId(10L, 1L);
    }

    @Test
    void shouldReturnEmptyWhenTransactionDoesNotExist() {

        when(transactionJpaRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.empty());
        Optional<Transaction> result = transactionRepositoryAdapter.findByIdAndUserId(10L, 1L);
        assertTrue(result.isEmpty());
        verify(transactionJpaRepository).findByIdAndUserId(10L, 1L);
    }

    @Test
    void shouldSaveTransaction() {

        Transaction transaction = mock(Transaction.class);
        Account account = mock(Account.class);
        Category category = mock(Category.class);
        AccountJpaEntity accountJpaEntity = mock(AccountJpaEntity.class);
        CategoryJpaEntity categoryJpaEntity = mock(CategoryJpaEntity.class);
        TransactionJpaEntity transactionJpaEntity = createValidTransactionJpaEntity();

        when(transaction.getAccount()).thenReturn(account);
        when(transaction.getCategory()).thenReturn(category);
        when(account.getId()).thenReturn(2L);
        when(category.getId()).thenReturn(3L);
        when(entityManager.getReference(AccountJpaEntity.class, 2L)).thenReturn(accountJpaEntity);
        when(entityManager.getReference(CategoryJpaEntity.class, 3L)).thenReturn(categoryJpaEntity);
        when(transactionJpaRepository.save(any(TransactionJpaEntity.class))).thenReturn(transactionJpaEntity);

        Transaction result = transactionRepositoryAdapter.save(transaction);

        assertNotNull(result);
        assertEquals("Compra", result.getDescription());
        assertEquals(new BigDecimal("100.00"), result.getAmount());

        verify(entityManager).getReference(AccountJpaEntity.class, 2L);
        verify(entityManager).getReference(CategoryJpaEntity.class, 3L);
        verify(transactionJpaRepository).save(any(TransactionJpaEntity.class));
    }

    @Test
    void shouldDeleteTransactionByIdAndUserId() {

        transactionRepositoryAdapter.deleteByIdAndUserId(10L, 1L);
        verify(transactionJpaRepository).deleteByIdAndUserId(10L, 1L);
    }

    private TransactionJpaEntity createValidTransactionJpaEntity() {

        TransactionJpaEntity entity = mock(TransactionJpaEntity.class);
        AccountJpaEntity account = mock(AccountJpaEntity.class);
        CategoryJpaEntity category = mock(CategoryJpaEntity.class);

        when(entity.getId()).thenReturn(10L);
        when(entity.getDescription()).thenReturn("Compra");
        when(entity.getAmount()).thenReturn(new BigDecimal("100.00"));
        when(entity.getTransactionDate()).thenReturn(LocalDate.of(2026, 9, 15));
        when(entity.getAccount()).thenReturn(account);
        when(entity.getCategory()).thenReturn(category);

        // Account
        when(account.getId()).thenReturn(2L);
        when(account.getName()).thenReturn("Conta corrente");
        when(account.getType()).thenReturn(AccountType.CONTA_CORRENTE);
        when(account.getBalance()).thenReturn(new BigDecimal("1000.00"));
        when(account.getUser()).thenReturn(UserJpaEntity.builder()
                .name("João Silva")
                .email("joao@gmail.com")
                .build());

        when(category.getId()).thenReturn(3L);
        when(category.getName()).thenReturn("Alimentação");
        when(category.getType()).thenReturn(CategoryType.SAIDA);
        when(category.getUser()).thenReturn(UserJpaEntity.builder()
                .name("João Silva")
                .email("joao@gmail.com")
                .build());

        return entity;
    }
}
