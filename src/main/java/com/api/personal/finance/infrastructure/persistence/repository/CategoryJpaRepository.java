package com.api.personal.finance.infrastructure.persistence.repository;

import com.api.personal.finance.infrastructure.persistence.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {

    List<CategoryJpaEntity> findByUserId(Long userId);

    Optional<CategoryJpaEntity> findByIdAndUserId(Long id, Long userId);
}