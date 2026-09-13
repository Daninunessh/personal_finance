package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Category {

    private Long id;
    private String name;
    private CategoryType type;
    private User user;

    @Builder
    public Category(Long id, String name, CategoryType type, User user) {
        validateName(name);
        validateType(type);
        validateUser(user);

        this.id = id;
        this.name = name;
        this.type = type;
        this.user = user;
    }

    public static Category create(String name, CategoryType type, User user) {
        return Category.builder()
                .name(name)
                .type(type)
                .user(user)
                .build();
    }

    public void updateInfo(String newName, CategoryType newType) {
        validateName(newName);
        validateType(newType);
        this.name = newName;
        this.type = newType;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDomainAttributeException("Nome da categoria não pode ser vazio.");
        }
    }

    private void validateType(CategoryType type) {
        if (type == null) {
            throw new InvalidDomainAttributeException("Tipo da categoria não pode ser nulo.");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidDomainAttributeException("O usuário não pode ser nulo.");
        }
    }
}
