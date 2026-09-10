package com.api.personal.finance.application.usecase;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.AccountType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.domain.exception.AccountNotFoundException;
import com.api.personal.finance.domain.exception.UserNotFoundException;
import com.api.personal.finance.domain.repository.AccountRepository;
import com.api.personal.finance.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<Account> getAccountsByUserId(Long userId) {
        ensureUserExists(userId);
        return accountRepository.findByUserId(userId);
    }

    public Account createAccount(Long userId, String name, AccountType type, BigDecimal initialBalance) {
        User user = ensureUserExists(userId);

        Account newAccount = Account.create(name, type, initialBalance, user);
        return accountRepository.save(newAccount);
    }

    public Account updateAccount(Long accountId, Long userId, String newName, AccountType newType) {
        Account existingAccount = getAccountByIdAndUserId(accountId, userId);

        existingAccount.updateInfo(newName, newType);
        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long accountId, Long userId) {
        Account existingAccount = getAccountByIdAndUserId(accountId, userId);
        accountRepository.deleteById(existingAccount.getId());
    }

    public Account getAccountByIdAndUserId(Long accountId, Long userId) {
        ensureUserExists(userId);
        return accountRepository.findByIdAndUserId(accountId, userId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private User ensureUserExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}