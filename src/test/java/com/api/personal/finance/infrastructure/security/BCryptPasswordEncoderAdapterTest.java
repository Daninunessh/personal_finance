package com.api.personal.finance.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BCryptPasswordEncoderAdapterTest {

    private final BCryptPasswordEncoderAdapter adapter = new BCryptPasswordEncoderAdapter();

    @Test
    @DisplayName("Deve criptografar e validar correspondência com sucesso")
    void shouldEncodeAndMatchPassword() {
        String rawPass = "123456";

        String encoded = adapter.encode(rawPass);

        assertThat(encoded).isNotEqualTo(rawPass);
        assertThat(adapter.matches(rawPass, encoded)).isTrue();
        assertThat(adapter.matches("wrongPass", encoded)).isFalse();
    }
}