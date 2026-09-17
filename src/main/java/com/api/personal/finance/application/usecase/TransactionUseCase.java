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
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class TransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<Transaction> getTransactionsByUserIdAndTransactionDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final");
        }
        ensureUserExists(userId);
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate);
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        ensureUserExists(userId);
        return transactionRepository.findByUserId(userId);
    }

    public Transaction getTransactionByIdAndUserId(Long transactionId, Long userId) {
        ensureUserExists(userId);
        return transactionRepository.findByIdAndUserId(transactionId, userId).orElseThrow(() -> new NotFoundException("Transação não encontrada para o ID: " + transactionId));
    }

    @Transactional
    public Transaction createTransaction(Long userId, String description, BigDecimal amount, LocalDate transactionDate, Long accountId, Long categoryId) {
        ensureUserExists(userId);

        Account account = accountRepository.findByIdAndUserId(accountId, userId).orElseThrow(() -> new NotFoundException("Conta não encontrada para o usuário."));
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId).orElseThrow(() -> new NotFoundException("Categoria não encontrada para o usuário."));

        Transaction newTransaction = Transaction.create(description, amount, transactionDate, account, category);
        account.applyTransaction(newTransaction.getAmount(), newTransaction.getCategory().getType());

        accountRepository.save(account);
        return transactionRepository.save(newTransaction);
    }

    @Transactional
    public Transaction updateTransaction(Long transactionId, Long userId, String description, BigDecimal amount, LocalDate transactionDate, Long newAccountId, Long newCategoryId) {
        Transaction existingTransaction = getTransactionByIdAndUserId(transactionId, userId);
        Account oldAccount = existingTransaction.getAccount();
        Category oldCategory = existingTransaction.getCategory();

        Account newAccount = accountRepository.findByIdAndUserId(newAccountId, userId).orElseThrow(() -> new NotFoundException("Conta não encontrada para o ID: " + newAccountId));
        Category newCategory = categoryRepository.findByIdAndUserId(newCategoryId, userId).orElseThrow(() -> new NotFoundException("Categoria não encontrada para o ID: " + newCategoryId));

        boolean sameAccount = oldAccount.getId().equals(newAccount.getId());

        oldAccount.reverseTransaction(existingTransaction.getAmount(), oldCategory.getType());

        if (sameAccount) {
            oldAccount.applyTransaction(amount, newCategory.getType());
        } else {
            newAccount.applyTransaction(amount, newCategory.getType());
        }

        existingTransaction.updateInfo(description, amount, transactionDate, newAccount, newCategory);

        accountRepository.save(oldAccount);

        if (!sameAccount) {
            accountRepository.save(newAccount);
        }
        return transactionRepository.save(existingTransaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction existingTransaction = getTransactionByIdAndUserId(transactionId, userId);
        Account account = existingTransaction.getAccount();
        Category category = existingTransaction.getCategory();

        account.reverseTransaction(existingTransaction.getAmount(), category.getType());

        accountRepository.save(account);
        transactionRepository.deleteByIdAndUserId(transactionId, userId);
    }

    private User ensureUserExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado para o ID: " + userId));
    }
}