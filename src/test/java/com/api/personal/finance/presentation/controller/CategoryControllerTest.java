package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.usecase.CategoryUseCase;
import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.entity.CategoryType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.presentation.dto.request.CategoryRequest;
import com.api.personal.finance.presentation.dto.response.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryUseCase categoryUseCase;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void shouldGetCategoriesByUserId() {
        when(categoryUseCase.getCategoriesByUserId(1L)).thenReturn(List.of(getCategory()));
        ResponseEntity<List<CategoryResponse>> response = categoryController.getCategoriesByUserId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void shouldGetCategoryByIdAndUserId() {
        when(categoryUseCase.getCategoryByIdAndUserId(1L, 1L)).thenReturn(getCategory());
        ResponseEntity<CategoryResponse> response = categoryController.getCategoryByIdAndUserId(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldCreateCategory() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Salário");
        categoryRequest.setType(CategoryType.ENTRADA);

        when(categoryUseCase.createCategory(1L, "Salário", CategoryType.ENTRADA)).thenReturn(getCategory());

        ResponseEntity<Void> response = categoryController.createCategory(1L, categoryRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).hasPath("/1");
    }

    @Test
    void shouldUpdateCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Salário Atualizado");
        request.setType(CategoryType.ENTRADA);

        ResponseEntity<Void> response = categoryController.updateCategory(1L, 1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(categoryUseCase).updateCategory(1L, 1L, "Salário Atualizado", CategoryType.ENTRADA);
    }

    @Test
    void shouldDeleteCategory() {
        ResponseEntity<Void> response = categoryController.deleteCategory(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(categoryUseCase).deleteCategory(1L, 1L);
    }

    private static Category getCategory() {
        return Category.builder()
                .id(1L)
                .name("Salário")
                .type(CategoryType.ENTRADA)
                .user(User.builder()
                        .id(1L)
                        .name("João")
                        .email("joao@gmail.com")
                        .password("123456")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build())
                .build();
    }
}