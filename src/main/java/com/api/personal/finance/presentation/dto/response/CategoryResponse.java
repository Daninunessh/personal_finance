package com.api.personal.finance.presentation.dto.response;

import com.api.personal.finance.domain.entity.Category;
import com.api.personal.finance.domain.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private CategoryType type;
    private Long userId;

    public static CategoryResponse fromDomain(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .userId(category.getUser().getId())
                .build();
    }
}