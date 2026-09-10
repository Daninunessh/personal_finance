package com.api.personal.finance.infrastructure.config;

import com.api.personal.finance.application.usecase.AccountUseCase;
import com.api.personal.finance.application.usecase.UserUseCase;
import com.api.personal.finance.domain.repository.AccountRepository;
import com.api.personal.finance.domain.repository.UserRepository;
import com.api.personal.finance.domain.security.PasswordEncoderPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class BeanConfigurationTest {

    private final BeanConfiguration beanConfiguration = new BeanConfiguration();

    @Test
    void shouldCreateBeansSuccessfully() {
        UserRepository userRepository = mock(UserRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        PasswordEncoderPort passwordEncoderPort = mock(PasswordEncoderPort.class);

        UserUseCase userUseCase = beanConfiguration.userUseCase(userRepository, passwordEncoderPort);
        AccountUseCase accountUseCase = beanConfiguration.accountUseCase(accountRepository, userRepository);

        assertNotNull(userUseCase);
        assertNotNull(accountUseCase);
    }
}