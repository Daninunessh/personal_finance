package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class Account {

    private Long id;
    private String name;
    private AccountType type;
    private BigDecimal balance;
    private Instant createdAt;
    private Instant updatedAt;
    private User user;

    @Builder
    public Account(Long id, String name, AccountType type, BigDecimal balance, Instant createdAt, Instant updatedAt, User user) {
        validateName(name);
        validateType(type);
        validateUser(user);

        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
    }

    public static Account create(String name, AccountType type, BigDecimal initialBalance, User user) {
        BigDecimal startingBalance = initialBalance != null ? initialBalance : BigDecimal.ZERO;
        if (startingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDomainAttributeException("O saldo inicial não pode ser negativo.");
        }

        Instant now = Instant.now();
        return Account.builder()
                .name(name)
                .type(type)
                .balance(startingBalance)
                .user(user)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void updateInfo(String newName, AccountType newType) {
        validateName(newName);
        validateType(newType);
        this.name = newName;
        this.type = newType;
        this.updatedAt = Instant.now();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDomainAttributeException("Nome da conta não pode ser vazio.");
        }
    }

    private void validateType(AccountType type) {
        if (type == null) {
            throw new InvalidDomainAttributeException("Tipo da conta não pode ser nulo.");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidDomainAttributeException("O usuário não pode ser nulo.");
        }
    }

    public void applyTransaction(BigDecimal amount, CategoryType categoryType) {
        validateTransactionAmount(amount);
        validateCategoryType(categoryType);

        if (categoryType == CategoryType.ENTRADA) {
            this.balance = this.balance.add(amount);
        } else if (categoryType == CategoryType.SAIDA) {
            this.balance = this.balance.subtract(amount);
        }
        this.updatedAt = Instant.now();
    }

    public void reverseTransaction(BigDecimal amount, CategoryType categoryType) {
        validateTransactionAmount(amount);
        validateCategoryType(categoryType);

        if (categoryType == CategoryType.ENTRADA) {
            this.balance = this.balance.subtract(amount);
        } else if (categoryType == CategoryType.SAIDA) {
            this.balance = this.balance.add(amount);
        }
        this.updatedAt = Instant.now();
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDomainAttributeException("O valor da transação não pode ser negativo.");
        }
    }

    private void validateCategoryType(CategoryType categoryType) {
        if (categoryType == null) {
            throw new InvalidDomainAttributeException("O tipo da categoria não pode ser nulo.");
        }
    }
}