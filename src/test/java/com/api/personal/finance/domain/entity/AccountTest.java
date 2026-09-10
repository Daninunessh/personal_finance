package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class AccountTest {

    @Test
    void create_ShouldCreateAccount_WhenParametersAreValid() {
        Account account = getAccount();

        assertNotNull(account);
        assertEquals("Nova Conta Corrente", account.getName());
        assertEquals(AccountType.CONTA_CORRENTE, account.getType());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    void create_ShouldSetBalanceToZero_WhenInitialBalanceIsNull() {
        Account account = Account.create("Carteira", AccountType.CARTEIRA, null, mock(User.class));
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void create_ShouldThrowException_WhenInitialBalanceIsNegative() {
        InvalidDomainAttributeException ex = assertThrows(
                InvalidDomainAttributeException.class,
                () -> Account.create("Conta", AccountType.CONTA_CORRENTE, new BigDecimal("-10.00"), mock(User.class))
        );
        assertEquals("O saldo inicial não pode ser negativo.", ex.getMessage());
    }

    @Test
    void builder_ShouldSetDefaultBalance_WhenBalanceIsNull() {
        Account account = Account.builder()
                .id(1L)
                .name("Investimentos")
                .type(AccountType.INVESTIMENTO)
                .balance(null)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .user(mock(User.class))
                .build();

        assertEquals(1L, account.getId());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void updateInfo_ShouldUpdateNameAndType() throws InterruptedException {
        Account account = getAccount();
        Instant oldUpdatedAt = account.getUpdatedAt();

        Thread.sleep(1);
        account.updateInfo("John Doe", AccountType.INVESTIMENTO);

        assertEquals("John Doe", account.getName());
        assertEquals(AccountType.INVESTIMENTO, account.getType());
        assertTrue(account.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    void validateName_ShouldThrowException_WhenNameIsInvalid() {
        assertThrows(InvalidDomainAttributeException.class, () -> Account.create(null, AccountType.CONTA_CORRENTE, BigDecimal.ZERO, mock(User.class)));
        assertThrows(InvalidDomainAttributeException.class, () -> Account.create("   ", AccountType.CONTA_CORRENTE, BigDecimal.ZERO, mock(User.class)));
    }

    @Test
    void validateType_ShouldThrowException_WhenTypeIsNull() {
        assertThrows(InvalidDomainAttributeException.class, () -> Account.create("Conta", null, BigDecimal.ZERO, mock(User.class)));
    }

    @Test
    void validateUser_ShouldThrowException_WhenUserIsNull() {
        assertThrows(InvalidDomainAttributeException.class, () -> Account.create("Conta", AccountType.CONTA_CORRENTE, BigDecimal.ZERO, null));
    }

    private static Account getAccount() {
        Account account = Account.create("Nova Conta Corrente", AccountType.CONTA_CORRENTE, new BigDecimal("1000.00"), mock(User.class));
        return account;
    }
}