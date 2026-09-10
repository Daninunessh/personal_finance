package com.api.personal.finance.application.usecase;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.AccountType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.domain.exception.AccountNotFoundException;
import com.api.personal.finance.domain.exception.UserNotFoundException;
import com.api.personal.finance.domain.repository.AccountRepository;
import com.api.personal.finance.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountUseCase accountUseCase;

    @Test
    void getAccountsByUserId_ShouldReturnAccounts_WhenUserExists() {
        Long userId = 1L;
        Account account = getAccount();

        when(userRepository.findById(userId)).thenReturn(Optional.of(account.getUser()));
        when(accountRepository.findByUserId(userId)).thenReturn(List.of(account));

        List<Account> result = accountUseCase.getAccountsByUserId(userId);

        assertEquals(1, result.size());
        verify(userRepository).findById(userId);
        verify(accountRepository).findByUserId(userId);
    }

    @Test
    void getAccountsByUserId_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> accountUseCase.getAccountsByUserId(1L));
        verify(accountRepository, never()).findByUserId(any());
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        Long userId = 1L;
        Account account = getAccount();

        when(userRepository.findById(userId)).thenReturn(Optional.of(account.getUser()));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountUseCase.createAccount(userId, "Invest", AccountType.INVESTIMENTO, BigDecimal.ZERO);

        assertNotNull(result);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount() {
        Long userId = 1L;
        Long accountId = 10L;
        Account account = getAccount();

        when(userRepository.findById(userId)).thenReturn(Optional.of(account.getUser()));
        when(accountRepository.findByIdAndUserId(accountId, userId)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account updated = accountUseCase.updateAccount(accountId, userId, "Novo", AccountType.CONTA_CORRENTE);

        assertEquals("Novo", updated.getName());
        assertEquals(AccountType.CONTA_CORRENTE, updated.getType());
        verify(accountRepository).save(account);
    }

    @Test
    void deleteAccount_ShouldDelete_WhenAccountExists() {
        Long userId = 1L;
        Long accountId = 10L;

        Account mockAccount = mock(Account.class);
        when(mockAccount.getId()).thenReturn(accountId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(accountRepository.findByIdAndUserId(accountId, userId)).thenReturn(Optional.of(mockAccount));

        accountUseCase.deleteAccount(accountId, userId);

        verify(accountRepository).deleteById(accountId);
    }

    @Test
    void getAccountByIdAndUserId_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mock(User.class)));
        when(accountRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountUseCase.getAccountByIdAndUserId(10L, 1L));
    }

    private static Account getAccount() {
        return Account.create("Principal", AccountType.CONTA_CORRENTE, BigDecimal.TEN, mock(User.class));
    }
}