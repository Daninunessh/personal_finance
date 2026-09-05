package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Instant createdAt;
    private Instant updatedAt;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Builder
    public User(Long id, String name, String email, String password, Instant createdAt, Instant updatedAt) {
        validateName(name);
        validateEmail(email);

        Instant now = Instant.now();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt != null ? createdAt : now;
        this.updatedAt = updatedAt != null ? updatedAt : now;
    }

    public void updateInfo(String newName, String newEmail, Instant updatedAt) {
        validateName(newName);
        validateEmail(newEmail);
        this.name = newName;
        this.email = newEmail;
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void updatePassword(String encodedPassword, Instant updatedAt) {
        if (encodedPassword == null || encodedPassword.trim().isEmpty()) {
            throw new InvalidDomainAttributeException("A senha processada não pode ser vazia.");
        }
        this.password = encodedPassword;
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new InvalidDomainAttributeException("E-mail em formato inválido.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDomainAttributeException("Nome não pode ser vazio.");
        }
    }
}