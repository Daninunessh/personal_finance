package com.api.personal.finance.infrastructure.exception;

import com.api.personal.finance.domain.exception.DomainException;
import com.api.personal.finance.domain.exception.EmailAlreadyExistsException;
import com.api.personal.finance.domain.exception.InvalidPasswordException;
import com.api.personal.finance.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class InfrastructureExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardError> userNotFound(UserNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), "Recurso não encontrado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<StandardError> emailAlreadyExists(EmailAlreadyExistsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), "Regra de Negócio", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<StandardError> invalidPassword(InvalidPasswordException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(Instant.now(), status.value(), "Acesso Negado", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<StandardError> domainException(DomainException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), "Erro no Domínio", e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        StandardError err = new StandardError(Instant.now(), status.value(), "Erro de Validação nos dados de entrada", errorMessage, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), "Conflito de Dados", "Operação não realizada pois violou restrições de integridade no banco de dados.", request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(Instant.now(), status.value(), "Erro Interno", "Ocorreu um erro interno no servidor.", request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}