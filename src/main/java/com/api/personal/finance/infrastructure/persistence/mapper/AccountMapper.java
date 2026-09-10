package com.api.personal.finance.infrastructure.persistence.mapper;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.infrastructure.persistence.entity.AccountJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;

public class AccountMapper {

    public static Account toDomain(AccountJpaEntity entity) {
        if (entity == null) return null;

        return Account.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .balance(entity.getBalance())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .user(UserMapper.toDomain(entity.getUser()))
                .build();
    }

    public static AccountJpaEntity toEntity(Account domain, UserJpaEntity userJpaEntity) {
        if (domain == null) return null;

        return AccountJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .type(domain.getType())
                .balance(domain.getBalance())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .user(userJpaEntity)
                .build();
    }
}