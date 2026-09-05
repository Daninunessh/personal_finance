package com.api.personal.finance.domain.entity;

import com.api.personal.finance.domain.exception.InvalidDomainAttributeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    @DisplayName("Deve criar um usuário válido com todas as propriedades")
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
    @DisplayName("Deve inicializar createdAt e updatedAt com Instant.now() quando forem nulos")
    void shouldInitializeDatesWhenNullInConstructor() {
        User user = new User(null, "João", "joao@email.com", "encodedPass", null, null);

        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Deve lançar exceção quando o nome for vazio ou apenas espaços")
    void shouldThrowExceptionWhenNameIsInvalid(String invalidName) {
        assertThatThrownBy(() -> new User(1L, invalidName, "joao@email.com", "pass", null, null))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("Nome não pode ser vazio.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome for nulo")
    void shouldThrowExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> new User(1L, null, "joao@email.com", "pass", null, null))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("Nome não pode ser vazio.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "joao@", "@email.com", "joao.com"})
    @DisplayName("Deve lançar exceção para formatos de e-mail inválidos")
    void shouldThrowExceptionWhenEmailIsInvalidFormat(String invalidEmail) {
        assertThatThrownBy(() -> new User(1L, "João", invalidEmail, "pass", null, null))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("E-mail em formato inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando e-mail for nulo")
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThatThrownBy(() -> new User(1L, "João", null, "pass", null, null))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("E-mail em formato inválido.");
    }

    @Test
    @DisplayName("Deve atualizar as informações do usuário com sucesso")
    void shouldUpdateInfoSuccessfully() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        Instant updateTime = Instant.now().plusSeconds(10);

        user.updateInfo("João Silva", "joaosilva@email.com", updateTime);

        assertThat(user.getName()).isEqualTo("João Silva");
        assertThat(user.getEmail()).isEqualTo("joaosilva@email.com");
        assertThat(user.getUpdatedAt()).isEqualTo(updateTime);
    }

    @Test
    @DisplayName("Deve usar Instant.now() no updateInfo caso updatedAt fornecido seja nulo")
    void shouldUpdateInfoWithDefaultNowWhenTimeIsNull() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());

        user.updateInfo("João Silva", "joaosilva@email.com", null);

        assertThat(user.getName()).isEqualTo("João Silva");
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve atualizar a senha do usuário com sucesso")
    void shouldUpdatePasswordSuccessfully() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        Instant updateTime = Instant.now().plusSeconds(10);

        user.updatePassword("newEncodedPass", updateTime);

        assertThat(user.getPassword()).isEqualTo("newEncodedPass");
        assertThat(user.getUpdatedAt()).isEqualTo(updateTime);
    }

    @Test
    @DisplayName("Deve usar Instant.now() no updatePassword caso updatedAt fornecido seja nulo")
    void shouldUpdatePasswordWithDefaultNowWhenTimeIsNull() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());

        user.updatePassword("newEncodedPass", null);

        assertThat(user.getPassword()).isEqualTo("newEncodedPass");
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Deve lançar exceção ao tentar atualizar senha para valor vazio")
    void shouldThrowExceptionWhenNewPasswordIsBlank(String invalidPass) {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());

        assertThatThrownBy(() -> user.updatePassword(invalidPass, Instant.now()))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("A senha processada não pode ser vazia.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar senha para nulo")
    void shouldThrowExceptionWhenNewPasswordIsNull() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());

        assertThatThrownBy(() -> user.updatePassword(null, Instant.now()))
                .isInstanceOf(InvalidDomainAttributeException.class)
                .hasMessage("A senha processada não pode ser vazia.");
    }
}