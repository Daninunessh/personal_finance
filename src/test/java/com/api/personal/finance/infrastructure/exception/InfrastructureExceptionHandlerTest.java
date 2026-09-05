package com.api.personal.finance.infrastructure.exception;

import com.api.personal.finance.domain.exception.DomainException;
import com.api.personal.finance.domain.exception.EmailAlreadyExistsException;
import com.api.personal.finance.domain.exception.InvalidPasswordException;
import com.api.personal.finance.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfrastructureExceptionHandlerTest {

    private final InfrastructureExceptionHandler handler = new InfrastructureExceptionHandler();

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Test
    @DisplayName("Deve tratar UserNotFoundException com NOT_FOUND")
    void shouldHandleUserNotFoundException() {
        when(request.getRequestURI()).thenReturn("/users/1");

        ResponseEntity<StandardError> response = handler.userNotFound(new UserNotFoundException(1L), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isEqualTo("Recurso não encontrado");
    }

    @Test
    @DisplayName("Deve tratar EmailAlreadyExistsException com CONFLICT")
    void shouldHandleEmailAlreadyExistsException() {
        when(request.getRequestURI()).thenReturn("/users");

        ResponseEntity<StandardError> response = handler.emailAlreadyExists(new EmailAlreadyExistsException("a@a.com"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("Deve tratar InvalidPasswordException com UNAUTHORIZED")
    void shouldHandleInvalidPasswordException() {
        when(request.getRequestURI()).thenReturn("/users/1/password");

        ResponseEntity<StandardError> response = handler.invalidPassword(new InvalidPasswordException("Senha incorreta"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("Deve tratar DomainException genérica com BAD_REQUEST")
    void shouldHandleDomainException() {
        when(request.getRequestURI()).thenReturn("/users");

        ResponseEntity<StandardError> response = handler.domainException(new DomainException("Erro domínio") {}, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException com UNPROCESSABLE_ENTITY")
    void shouldHandleValidationError() {
        when(request.getRequestURI()).thenReturn("/users");
        FieldError fieldError = new FieldError("userRequest", "email", "E-mail em formato inválido");
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<StandardError> response = handler.validationError(methodArgumentNotValidException, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getMessage()).contains("email: E-mail em formato inválido");
    }

    @Test
    @DisplayName("Deve tratar DataIntegrityViolationException com CONFLICT")
    void shouldHandleDataIntegrityViolation() {
        when(request.getRequestURI()).thenReturn("/users");

        ResponseEntity<StandardError> response = handler.dataIntegrityViolation(new DataIntegrityViolationException("Erro de banco"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("Deve tratar Exception genérica com INTERNAL_SERVER_ERROR")
    void shouldHandleGenericException() {
        when(request.getRequestURI()).thenReturn("/users");

        ResponseEntity<StandardError> response = handler.genericException(new RuntimeException("Erro inesperado"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }
}