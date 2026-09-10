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
            throw new InvalidDomainAttributeException("Nome não pode ser vazio.");
        }
    }

    private void validateType(AccountType type) {
        if (type == null) {
            throw new InvalidDomainAttributeException("Tipo não pode ser nulo.");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidDomainAttributeException("O usuário não pode ser nulo.");
        }
    }
}