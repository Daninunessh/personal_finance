package com.api.personal.finance.application.usecase;

import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.entity.CategoryType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.domain.exception.NotFoundException;
import com.api.personal.finance.domain.repository.CategoryRepository;
import com.api.personal.finance.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<Category> getCategoriesByUserId(Long userId) {
        ensureUserExists(userId);
        return categoryRepository.findByUserId(userId);
    }

    public Category getCategoryByIdAndUserId(Long categoryId, Long userId) {
        ensureUserExists(userId);
        return categoryRepository.findByIdAndUserId(categoryId, userId).orElseThrow(() -> new NotFoundException("Categoria não encontrada para o ID: " + categoryId));
    }

    public Category createCategory(Long userId, String name, CategoryType type) {
        User user = ensureUserExists(userId);

        Category newCategory = Category.create(name, type, user);
        return categoryRepository.save(newCategory);
    }

    public Category updateCategory(Long categoryId, Long userId, String newName, CategoryType newType) {
        Category existingCategory = getCategoryByIdAndUserId(categoryId, userId);

        existingCategory.updateInfo(newName, newType);
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long categoryId, Long userId) {
        Category existingCategory = getCategoryByIdAndUserId(categoryId, userId);
        categoryRepository.deleteById(existingCategory.getId());
    }

    private User ensureUserExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado para o ID: " + userId));
    }
}