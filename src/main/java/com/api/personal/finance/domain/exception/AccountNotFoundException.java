package com.api.personal.finance.domain.exception;

public class AccountNotFoundException extends DomainException {
    public AccountNotFoundException(Long accountId) {
        super("Conta não encontrada para o ID: " + accountId);
    }
}