package com.api.personal.finance.infrastructure.persistence.mapper;

import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.infrastructure.persistence.entity.CategoryJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;

public class CategoryMapper {

    public static Category toDomain(CategoryJpaEntity entity) {
        if (entity == null) return null;

        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .user(UserMapper.toDomain(entity.getUser()))
                .build();
    }

    public static CategoryJpaEntity toEntity(Category domain, UserJpaEntity userJpaEntity) {
        if (domain == null) return null;

        return CategoryJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .type(domain.getType())
                .user(userJpaEntity)
                .build();
    }
}