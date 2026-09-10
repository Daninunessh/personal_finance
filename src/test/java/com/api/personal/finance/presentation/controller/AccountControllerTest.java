package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.usecase.AccountUseCase;
import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.AccountType;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.presentation.dto.request.AccountRequest;
import com.api.personal.finance.presentation.dto.request.AccountUpdateRequest;
import com.api.personal.finance.presentation.dto.response.AccountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountUseCase accountUseCase;

    @InjectMocks
    private AccountController accountController;

    @Test
    void shouldGetAccountsByUserId() {
        when(accountUseCase.getAccountsByUserId(1L)).thenReturn(List.of(getAccount()));
        ResponseEntity<List<AccountResponse>> response = accountController.getAccountsByUserId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void shouldGetAccountByIdAndUserIdAndUserId() {
        when(accountUseCase.getAccountByIdAndUserId(1L, 1L)).thenReturn(getAccount());
        ResponseEntity<AccountResponse> response = accountController.getAccountByIdAndUserId(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldCreateUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Conta Corrente");
        accountRequest.setType(AccountType.CONTA_CORRENTE);
        accountRequest.setInitialBalance(BigDecimal.TEN);

        when(accountUseCase.createAccount(1L, "Conta Corrente", AccountType.CONTA_CORRENTE, BigDecimal.TEN)).thenReturn(getAccount());

        ResponseEntity<Void> response = accountController.createAccount(1L, accountRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).hasPath("/1");
    }

    @Test
    void shouldUpdateUser() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setName("Conta Corrente Atualizada");
        request.setType(AccountType.CONTA_CORRENTE);

        ResponseEntity<Void> response = accountController.updateAccount(1L, 1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(accountUseCase).updateAccount(1L, 1L, "Conta Corrente Atualizada", AccountType.CONTA_CORRENTE);
    }

    @Test
    void shouldDeleteAccount() {
        ResponseEntity<Void> response = accountController.deleteAccount(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(accountUseCase).deleteAccount(1L, 1L);
    }

    private static Account getAccount() {
        return Account.builder()
                .id(1L)
                .name("Conta Corrente")
                .type(AccountType.CONTA_CORRENTE)
                .balance(BigDecimal.TEN)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .user(User.builder()
                        .id(1L)
                        .name("João")
                        .email("joao@gmail.com")
                        .password("123456")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build())
                .build();
    }
}