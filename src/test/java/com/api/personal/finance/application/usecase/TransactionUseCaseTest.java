package com.api.personal.finance.application.usecase;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.entity.Transaction;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.domain.exception.NotFoundException;
import com.api.personal.finance.domain.repository.AccountRepository;
import com.api.personal.finance.domain.repository.CategoryRepository;
import com.api.personal.finance.domain.repository.TransactionRepository;
import com.api.personal.finance.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionUseCase transactionUseCase;

    @Test
    void shouldReturnTransactionsByUserId() {

        User user = getUser(1L);
        Transaction transaction = getTransaction();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByUserId(1L)).thenReturn(List.of(transaction));
        List<Transaction> result = transactionUseCase.getTransactionsByUserId(1L);

        assertNotNull(result);
        assertEquals(transaction, result.get(0));

        verify(userRepository).findById(1L);
        verify(transactionRepository).findByUserId(1L);
    }

    @Test
    void shouldReturnTransactionsByUserIdAndDateBetween() {

        User user = getUser(1L);
        Transaction transaction = getTransaction();

        LocalDate startDate = LocalDate.of(2026, 9, 1);
        LocalDate endDate = LocalDate.of(2026, 9, 30);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(transactionRepository.findByUserIdAndTransactionDateBetween(1L, startDate, endDate)).thenReturn(List.of(transaction));
        List<Transaction> result = transactionUseCase.getTransactionsByUserIdAndTransactionDateBetween(1L, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(userRepository).findById(1L);
        verify(transactionRepository).findByUserIdAndTransactionDateBetween(1L, startDate, endDate);
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {

        LocalDate startDate = LocalDate.of(2026, 9, 30);
        LocalDate endDate = LocalDate.of(2026, 9, 1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> transactionUseCase.getTransactionsByUserIdAndTransactionDateBetween(1L, startDate, endDate));

        assertEquals("A data inicial não pode ser posterior à data final", exception.getMessage());

        verifyNoInteractions(userRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionUseCase.getTransactionsByUserId(1L));

        assertEquals("Usuário não encontrado para o ID: 1", exception.getMessage());

        verify(userRepository).findById(1L);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldReturnTransactionByIdAndUserId() {

        User user = getUser(1L);
        Transaction transaction = getTransaction();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(transaction));
        Transaction result = transactionUseCase.getTransactionByIdAndUserId(10L, 1L);

        assertNotNull(result);
        assertEquals(transaction, result);

        verify(userRepository).findById(1L);
        verify(transactionRepository).findByIdAndUserId(10L, 1L);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenTransactionDoesNotExist() {

        User user = getUser(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionUseCase.getTransactionByIdAndUserId(10L, 1L));
        assertEquals("Transação não encontrada para o ID: 10", exception.getMessage());

        verify(userRepository).findById(1L);
        verify(transactionRepository).findByIdAndUserId(10L, 1L);
    }

    @Test
    void shouldCreateTransactionAndUpdateAccountBalance() {

        User user = getUser(1L);
        Account account = mock(Account.class);
        Category category = mock(Category.class);
        Transaction transaction = getTransaction();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findByIdAndUserId(3L, 1L)).thenReturn(Optional.of(category));
        when(category.getType()).thenReturn(null);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionUseCase.createTransaction(1L, "Compra", new BigDecimal("100.00"),
                LocalDate.of(2026, 9, 15), 2L, 3L);

        assertNotNull(result);
        assertEquals(transaction, result);

        verify(account).applyTransaction(any(BigDecimal.class), isNull());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenAccountDoesNotExist() {

        User user = getUser(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> transactionUseCase.createTransaction(1L, "Compra", new BigDecimal("100.00"),
                        LocalDate.of(2026, 9, 15), 2L, 3L));
        assertEquals("Conta não encontrada para o usuário.", exception.getMessage());

        verify(accountRepository).findByIdAndUserId(2L, 1L);
        verifyNoInteractions(categoryRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCategoryDoesNotExist() {

        User user = getUser(1L);
        Account account = mock(Account.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findByIdAndUserId(3L, 1L)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> transactionUseCase.createTransaction(1L, "Compra",
                        new BigDecimal("100.00"), LocalDate.of(2026, 9, 15), 2L, 3L));

        assertEquals("Categoria não encontrada para o usuário.", exception.getMessage());

        verify(categoryRepository).findByIdAndUserId(3L, 1L);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldUpdateTransactionInSameAccount() {

        User user = getUser(1L);
        Account account = mock(Account.class);
        Category oldCategory = mock(Category.class);
        Category newCategory = mock(Category.class);
        Transaction existingTransaction = mock(Transaction.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(existingTransaction));
        when(existingTransaction.getAccount()).thenReturn(account);
        when(existingTransaction.getCategory()).thenReturn(oldCategory);
        when(existingTransaction.getAmount()).thenReturn(new BigDecimal("50.00"));
        when(account.getId()).thenReturn(2L);
        when(accountRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findByIdAndUserId(3L, 1L)).thenReturn(Optional.of(newCategory));
        when(oldCategory.getType()).thenReturn(null);
        when(newCategory.getType()).thenReturn(null);
        when(transactionRepository.save(existingTransaction)).thenReturn(existingTransaction);

        Transaction result = transactionUseCase.updateTransaction(10L, 1L, "Compra atualizada",
                new BigDecimal("80.00"), LocalDate.of(2026, 9, 16), 2L, 3L);

        assertNotNull(result);

        verify(account).reverseTransaction(new BigDecimal("50.00"), null);
        verify(account).applyTransaction(new BigDecimal("80.00"), null);
        verify(existingTransaction).updateInfo("Compra atualizada", new BigDecimal("80.00"), LocalDate.of(2026, 9, 16), account, newCategory);

        verify(accountRepository).save(account);
        verify(transactionRepository).save(existingTransaction);
    }

    @Test
    void shouldUpdateTransactionInDifferentAccounts() {

        User user = getUser(1L);
        Account oldAccount = mock(Account.class);
        Account newAccount = mock(Account.class);
        Category oldCategory = mock(Category.class);
        Category newCategory = mock(Category.class);
        Transaction existingTransaction = mock(Transaction.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(existingTransaction));
        when(existingTransaction.getAccount()).thenReturn(oldAccount);
        when(existingTransaction.getCategory()).thenReturn(oldCategory);
        when(existingTransaction.getAmount()).thenReturn(new BigDecimal("50.00"));
        when(oldAccount.getId()).thenReturn(2L);
        when(newAccount.getId()).thenReturn(5L);
        when(accountRepository.findByIdAndUserId(5L, 1L)).thenReturn(Optional.of(newAccount));
        when(categoryRepository.findByIdAndUserId(3L, 1L)).thenReturn(Optional.of(newCategory));
        when(oldCategory.getType()).thenReturn(null);
        when(newCategory.getType()).thenReturn(null);
        when(transactionRepository.save(existingTransaction)).thenReturn(existingTransaction);

        Transaction result = transactionUseCase.updateTransaction(10L, 1L, "Compra atualizada", new BigDecimal("80.00"), LocalDate.of(2026, 9, 16), 5L, 3L);

        assertNotNull(result);

        verify(oldAccount).reverseTransaction(new BigDecimal("50.00"), null);
        verify(newAccount).applyTransaction(new BigDecimal("80.00"), null);
        verify(oldAccount, never()).applyTransaction(any(), any());
        verify(accountRepository).save(oldAccount);
        verify(accountRepository).save(newAccount);
        verify(transactionRepository).save(existingTransaction);
    }

    @Test
    void shouldDeleteTransactionAndReverseAccountBalance() {

        User user = getUser(1L);
        Account account = mock(Account.class);
        Category category = mock(Category.class);
        Transaction transaction = mock(Transaction.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(transactionRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(transaction));
        when(transaction.getAccount()).thenReturn(account);
        when(transaction.getCategory()).thenReturn(category);
        when(transaction.getAmount()).thenReturn(new BigDecimal("100.00"));
        when(category.getType()).thenReturn(null);

        transactionUseCase.deleteTransaction(10L, 1L);

        verify(account).reverseTransaction(new BigDecimal("100.00"), null);
        verify(accountRepository).save(account);
        verify(transactionRepository).deleteByIdAndUserId(10L, 1L);
    }

    private static User getUser(Long id) {
        return User.builder()
                .id(id)
                .name("João")
                .email("joao@email.com")
                .build();
    }

    private static Transaction getTransaction() {
        return mock(Transaction.class);
    }
}