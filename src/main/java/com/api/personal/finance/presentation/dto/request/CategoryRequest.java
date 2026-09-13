package com.api.personal.finance.presentation.dto.request;

import com.api.personal.finance.domain.entity.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "O nome da categoria é obrigatório")
    private String name;

    @NotNull(message = "O tipo da categoria é obrigatório")
    private CategoryType type;
}
