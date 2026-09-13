package com.api.personal.finance.domain.repository;

import com.api.personal.finance.domain.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> findByUserId(Long userId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    Category save(Category category);

    void deleteById(Long id);
}