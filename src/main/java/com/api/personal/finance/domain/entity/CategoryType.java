package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {

    ENTRADA("Entrada"),
    SAIDA("Saída");

    @JsonValue
    private final String name;

    @JsonCreator
    public static CategoryType fromName(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        for (CategoryType type : CategoryType.values()) {
            if (type.name.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new InvalidDomainAttributeException("Tipo de categoria inválido: " + value);
    }
}