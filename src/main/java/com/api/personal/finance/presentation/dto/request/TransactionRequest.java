package com.api.personal.finance.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionRequest {

    @NotBlank(message = "A descrição da transação é obrigatória")
    private String description;

    @NotNull(message = "O valor da transação é obrigatório")
    @PositiveOrZero(message = "O valor da transação não pode ser negativo")
    private BigDecimal amount;

    @NotNull(message = "A data da transação é obrigatória")
    private LocalDate transactionDate;

    @NotNull(message = "A conta da transação é obrigatória")
    private Long accountId;

    @NotNull(message = "A categoria da transação é obrigatória")
    private Long categoryId;
}
