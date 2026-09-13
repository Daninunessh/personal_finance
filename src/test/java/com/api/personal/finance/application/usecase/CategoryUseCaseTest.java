package com.api.personal.finance.application.usecase;

import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.entity.CategoryType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.domain.exception.NotFoundException;
import com.api.personal.finance.domain.repository.CategoryRepository;
import com.api.personal.finance.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    @Test
    void getCategorysByUserId_ShouldReturnCategories_WhenUserExists() {
        Long userId = 1L;
        Category category = getCategory();

        when(userRepository.findById(userId)).thenReturn(Optional.of(category.getUser()));
        when(categoryRepository.findByUserId(userId)).thenReturn(List.of(category));

        List<Category> result = categoryUseCase.getCategoriesByUserId(userId);

        assertEquals(1, result.size());
        verify(userRepository).findById(userId);
        verify(categoryRepository).findByUserId(userId);
    }

    @Test
    void getCategoriesByUserId_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryUseCase.getCategoriesByUserId(1L));
        verify(categoryRepository, never()).findByUserId(any());
    }

    @Test
    void createCategory_ShouldReturnCreatedCategory() {
        Long userId = 1L;
        Category category = getCategory();

        when(userRepository.findById(userId)).thenReturn(Optional.of(category.getUser()));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryUseCase.createCategory(userId, "Aluguel", CategoryType.SAIDA);

        assertNotNull(result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() {
        Long userId = 1L;
        Long categoryId = 10L;
        Category category = getCategory();

        when(userRepository.findById(userId)).thenReturn(Optional.of(category.getUser()));
        when(categoryRepository.findByIdAndUserId(categoryId, userId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        Category updated = categoryUseCase.updateCategory(categoryId, userId, "Salário", CategoryType.ENTRADA);
        assertEquals("Salário", updated.getName());
        assertEquals(CategoryType.ENTRADA, updated.getType());
        verify(categoryRepository).save(category);
    }

    @Test
    void deleteCategory_ShouldDelete_WhenCategoryExists() {
        Long userId = 1L;
        Long categoryId = 10L;

        Category mockCategory = mock(Category.class);
        when(mockCategory.getId()).thenReturn(categoryId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(categoryRepository.findByIdAndUserId(categoryId, userId)).thenReturn(Optional.of(mockCategory));

        categoryUseCase.deleteCategory(categoryId, userId);

        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void getCategoryByIdAndUserId_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mock(User.class)));
        when(categoryRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryUseCase.getCategoryByIdAndUserId(10L, 1L));
    }

    private static Category getCategory() {
        return Category.create("Salário", CategoryType.ENTRADA, mock(User.class));
    }
}