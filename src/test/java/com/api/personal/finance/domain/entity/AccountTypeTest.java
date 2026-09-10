package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AccountTypeTest {

    @Test
    void getDescription_ShouldReturnCorrectDescription() {
        assertEquals("Conta corrente", AccountType.CONTA_CORRENTE.getDescription());
        assertEquals("Poupança", AccountType.POUPANCA.getDescription());
        assertEquals("Carteira", AccountType.CARTEIRA.getDescription());
        assertEquals("Investimento", AccountType.INVESTIMENTO.getDescription());
    }

    @Test
    void fromDescription_ShouldReturnEnum_WhenValidValue() {
        assertEquals(AccountType.CONTA_CORRENTE, AccountType.fromDescription("Conta corrente"));
        assertEquals(AccountType.CONTA_CORRENTE, AccountType.fromDescription("CONTA_CORRENTE"));
        assertEquals(AccountType.POUPANCA, AccountType.fromDescription("Poupança"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void fromDescription_ShouldReturnNull_WhenValueIsBlankOrNull(String value) {
        assertNull(AccountType.fromDescription(value));
        assertNull(AccountType.fromDescription(null));
    }

    @Test
    void fromDescription_ShouldThrowException_WhenValueIsInvalid() {
        InvalidDomainAttributeException ex = assertThrows(
                InvalidDomainAttributeException.class,
                () -> AccountType.fromDescription("TIPO_INVALIDO")
        );

        assertEquals("Tipo de conta inválido: TIPO_INVALIDO", ex.getMessage());
    }
}