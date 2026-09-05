package com.api.personal.finance.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @Email(message = "E-mail em formato inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    private String email;
}
