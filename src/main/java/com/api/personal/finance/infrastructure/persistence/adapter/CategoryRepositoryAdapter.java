package com.api.personal.finance.infrastructure.persistence.adapter;

import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.repository.CategoryRepository;
import com.api.personal.finance.infrastructure.persistence.entity.CategoryJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import com.api.personal.finance.infrastructure.persistence.mapper.CategoryMapper;
import com.api.personal.finance.infrastructure.persistence.repository.CategoryJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Category> findByUserId(Long userId) {
        return categoryJpaRepository.findByUserId(userId)
                .stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Category> findByIdAndUserId(Long id, Long userId) {
        return categoryJpaRepository.findByIdAndUserId(id, userId).map(CategoryMapper::toDomain);
    }

    @Override
    public Category save(Category category) {
        UserJpaEntity userRef = entityManager.getReference(UserJpaEntity.class, category.getUser().getId());
        CategoryJpaEntity entity = CategoryMapper.toEntity(category, userRef);
        return CategoryMapper.toDomain(categoryJpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }
}