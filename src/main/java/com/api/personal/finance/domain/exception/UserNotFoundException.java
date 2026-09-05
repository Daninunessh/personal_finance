package com.api.personal.finance.domain.exception;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(Long id) {
        super("Usuário não encontrado para o ID: " + id);
    }
}