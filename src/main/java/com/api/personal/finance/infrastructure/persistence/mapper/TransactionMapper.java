package com.api.personal.finance.infrastructure.persistence.mapper;

import com.api.personal.finance.domain.entity.Transaction;
import com.api.personal.finance.infrastructure.persistence.entity.AccountJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.CategoryJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.TransactionJpaEntity;

public class TransactionMapper {

    public static Transaction toDomain(TransactionJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Transaction.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .amount(entity.getAmount())
                .transactionDate(entity.getTransactionDate())
                .account(AccountMapper.toDomain(entity.getAccount()))
                .category(CategoryMapper.toDomain(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TransactionJpaEntity toEntity(Transaction domain, AccountJpaEntity accountJpaEntity, CategoryJpaEntity categoryJpaEntity) {
        if (domain == null) {
            return null;
        }

        return TransactionJpaEntity.builder()
                .id(domain.getId())
                .description(domain.getDescription())
                .amount(domain.getAmount())
                .transactionDate(domain.getTransactionDate())
                .account(accountJpaEntity)
                .category(categoryJpaEntity)
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}