package com.api.personal.finance.domain.security;

public interface PasswordEncoderPort {

    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
