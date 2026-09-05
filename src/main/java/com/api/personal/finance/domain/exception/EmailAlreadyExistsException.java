package com.api.personal.finance.domain.exception;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("O e-mail '" + email + "' já está cadastrado.");
    }
}
