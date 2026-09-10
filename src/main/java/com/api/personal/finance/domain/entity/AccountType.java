package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {

    CONTA_CORRENTE("Conta corrente"),
    POUPANCA("Poupança"),
    CARTEIRA("Carteira"),
    INVESTIMENTO("Investimento");

    @JsonValue
    private final String description;

    @JsonCreator
    public static AccountType fromDescription(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (AccountType type : AccountType.values()) {
            if (type.description.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new InvalidDomainAttributeException("Tipo de conta inválido: " + value);
    }
}