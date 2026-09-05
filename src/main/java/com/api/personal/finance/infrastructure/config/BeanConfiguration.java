package com.api.personal.finance.infrastructure.config;

import com.api.personal.finance.application.usecase.UserUseCase;
import com.api.personal.finance.domain.repository.UserRepository;
import com.api.personal.finance.domain.security.PasswordEncoderPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public UserUseCase userUseCase(UserRepository userRepository, PasswordEncoderPort passwordEncoderPort) {
        return new UserUseCase(userRepository, passwordEncoderPort);
    }
}
