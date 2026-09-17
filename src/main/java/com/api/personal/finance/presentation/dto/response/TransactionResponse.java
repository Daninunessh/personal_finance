package com.api.personal.finance.presentation.dto.response;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.AccountType;
import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.entity.CategoryType;
import com.api.personal.finance.domain.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private AccountSummaryResponse account;
    private CategorySummaryResponse category;
    private Instant createdAt;
    private Instant updatedAt;

    public static TransactionResponse fromDomain(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .account(Optional.ofNullable(transaction.getAccount())
                        .map(AccountSummaryResponse::fromDomain)
                        .orElse(null))
                .category(Optional.ofNullable(transaction.getCategory())
                        .map(CategorySummaryResponse::fromDomain)
                        .orElse(null))
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    @Builder @Getter @AllArgsConstructor @NoArgsConstructor
    public static class AccountSummaryResponse {
        private Long id;
        private String name;
        private AccountType type;
        private BigDecimal balance;
        private Instant createdAt;
        private Instant updatedAt;

        public static AccountSummaryResponse fromDomain(Account account) {
            return new AccountSummaryResponse(account.getId(), account.getName(), account.getType(), account.getBalance(), account.getCreatedAt(), account.getUpdatedAt());
        }
    }

    @Builder @Getter @AllArgsConstructor @NoArgsConstructor
    public static class CategorySummaryResponse {
        private Long id;
        private String name;
        private CategoryType type;

        public static CategorySummaryResponse fromDomain(Category category) {
            return new CategorySummaryResponse(category.getId(), category.getName(), category.getType());
        }
    }
}


