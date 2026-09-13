package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.usecase.CategoryUseCase;
import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.presentation.dto.request.CategoryRequest;
import com.api.personal.finance.presentation.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategoriesByUserId(@PathVariable Long userId) {
        List<CategoryResponse> categories = categoryUseCase.getCategoriesByUserId(userId)
                .stream()
                .map(CategoryResponse::fromDomain)
                .toList();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryByIdAndUserId(@PathVariable Long userId, @PathVariable Long categoryId) {
        Category category = categoryUseCase.getCategoryByIdAndUserId(categoryId, userId);
        return ResponseEntity.ok().body(CategoryResponse.fromDomain(category));
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(@PathVariable Long userId, @Valid @RequestBody CategoryRequest request) {
        Category createdCategory = categoryUseCase.createCategory(userId, request.getName(), request.getType());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{categoryId}").buildAndExpand(createdCategory.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long userId, @PathVariable Long categoryId, @Valid @RequestBody CategoryRequest request) {
        categoryUseCase.updateCategory(categoryId, userId, request.getName(), request.getType());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long userId, @PathVariable Long categoryId) {
        categoryUseCase.deleteCategory(categoryId, userId);
        return ResponseEntity.noContent().build();
    }
}