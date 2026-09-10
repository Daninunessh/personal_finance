package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void shouldCreateValidUser() {
        Instant now = Instant.now();
        User user = new User(1L, "João", "joao@email.com", "encodedPass", now, now);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("João");
        assertThat(user.getEmail()).isEqualTo("joao@email.com");
        assertThat(user.getPassword()).isEqualTo("encodedPass");
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void shouldInitializeDatesWhenNullInConstructor() {
        User user = new User(null, "João", "joao@email.com", "encodedPass", null, null);

        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldThrowExceptionWhenNameIsInvalid(String invalidName) {
        assertThatThrownBy(() -> new User(1L, invalidName, "joao@email.com", "pass", null, null))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("Nome não pode ser vazio.");
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new User(1L, null, "joao@email.com", "pass", null, null))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("Nome não pode ser vazio.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "joao@", "@email.com", "joao.com"})
    void shouldThrowExceptionWhenEmailIsInvalidFormat(String invalidEmail) {
        assertThatThrownBy(() -> new User(1L, "João", invalidEmail, "pass", null, null))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("E-mail em formato inválido.");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThatThrownBy(() -> new User(1L, "João", null, "pass", null, null))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("E-mail em formato inválido.");
    }

    @Test
    void shouldUpdateInfoSuccessfully() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        Instant updateTime = Instant.now().plusSeconds(10);

        user.updateInfo("João Silva", "joaosilva@email.com", updateTime);

        assertThat(user.getName()).isEqualTo("João Silva");
        assertThat(user.getEmail()).isEqualTo("joaosilva@email.com");
        assertThat(user.getUpdatedAt()).isEqualTo(updateTime);
    }

    @Test
    void shouldUpdateInfoWithDefaultNowWhenTimeIsNull() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());

        user.updateInfo("João Silva", "joaosilva@email.com", null);

        assertThat(user.getName()).isEqualTo("João Silva");
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        Instant updateTime = Instant.now().plusSeconds(10);

        user.updatePassword("newEncodedPass", updateTime);

        assertThat(user.getPassword()).isEqualTo("newEncodedPass");
        assertThat(user.getUpdatedAt()).isEqualTo(updateTime);
    }

    @Test
    void shouldUpdatePasswordWithDefaultNowWhenTimeIsNull() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());

        user.updatePassword("newEncodedPass", null);

        assertThat(user.getPassword()).isEqualTo("newEncodedPass");
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldThrowExceptionWhenNewPasswordIsBlank(String invalidPass) {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());

        assertThatThrownBy(() -> user.updatePassword(invalidPass, Instant.now()))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("A senha processada não pode ser vazia.");
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordIsNull() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());

        assertThatThrownBy(() -> user.updatePassword(null, Instant.now()))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("A senha processada não pode ser vazia.");
    }
}