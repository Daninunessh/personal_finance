package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
public class Transaction {

    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private Instant createdAt;
    private Instant updatedAt;
    private Account account;
    private Category category;

    @Builder
    public Transaction(Long id, String description, BigDecimal amount, LocalDate transactionDate, Instant createdAt, Instant updatedAt, Account account, Category category) {
        validateDescription(description);
        validateAmount(amount);
        validateAccount(account);
        validateCategory(category);

        this.id = id;
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.account = account;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public static Transaction create(String description, BigDecimal amount, LocalDate transactionDate, Account account, Category category) {
        BigDecimal startingAmount = amount != null ? amount : BigDecimal.ZERO;
        if (startingAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDomainAttributeException("O valor da transação não pode ser negativo.");
        }

        return Transaction.builder()
                .description(description)
                .amount(startingAmount)
                .transactionDate(transactionDate)
                .account(account)
                .category(category)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public void updateInfo(String newDescription, BigDecimal newAmount, LocalDate newTransactionDate, Account account, Category category) {
        validateDescription(newDescription);
        validateAmount(newAmount);
        validateAccount(account);
        validateCategory(category);

        this.description = newDescription;
        this.amount = newAmount;
        this.transactionDate = newTransactionDate;
        this.account = account;
        this.category = category;
        this.updatedAt = Instant.now();
    }

    private void validateAccount(Account account) {
        if (account == null) {
            throw new InvalidDomainAttributeException("A conta não pode ser nula.");
        }
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new InvalidDomainAttributeException("A categoria não pode ser nula.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidDomainAttributeException("Descrição da transação não pode ser vazia.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidDomainAttributeException("Valor da transação não pode ser nulo.");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDomainAttributeException("O valor da transação não pode ser negativo.");
        }
    }
}
