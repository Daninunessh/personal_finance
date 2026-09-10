package com.api.personal.finance.presentation.dto.request;

import com.api.personal.finance.domain.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {

    @NotBlank(message = "O nome da conta é obrigatório")
    private String name;

    @NotNull(message = "O tipo da conta é obrigatório")
    private AccountType type;

    @NotNull(message = "O saldo inicial é obrigatório")
    @PositiveOrZero(message = "O saldo inicial não pode ser negativo")
    private BigDecimal initialBalance;
}
