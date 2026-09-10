package com.api.personal.finance.presentation.dto.request;

import com.api.personal.finance.domain.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateRequest {

    @NotBlank(message = "O nome da conta é obrigatório")
    private String name;

    @NotNull(message = "O tipo da conta é obrigatório")
    private AccountType type;
}
