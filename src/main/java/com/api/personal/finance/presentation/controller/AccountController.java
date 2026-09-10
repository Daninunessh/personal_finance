package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.usecase.AccountUseCase;
import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.presentation.dto.request.AccountRequest;
import com.api.personal.finance.presentation.dto.request.AccountUpdateRequest;
import com.api.personal.finance.presentation.dto.response.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(@PathVariable Long userId) {
        List<AccountResponse> accounts = accountUseCase.getAccountsByUserId(userId)
                .stream()
                .map(AccountResponse::fromDomain)
                .toList();
        return ResponseEntity.ok().body(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountByIdAndUserId(@PathVariable Long userId, @PathVariable Long accountId) {
        Account account = accountUseCase.getAccountByIdAndUserId(accountId, userId);
        return ResponseEntity.ok().body(AccountResponse.fromDomain(account));
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@PathVariable Long userId, @Valid @RequestBody AccountRequest request) {
        Account createdAccount = accountUseCase.createAccount(userId, request.getName(), request.getType(), request.getInitialBalance());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{accountId}").buildAndExpand(createdAccount.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Void> updateAccount(@PathVariable Long userId, @PathVariable Long accountId, @Valid @RequestBody AccountUpdateRequest request) {
        accountUseCase.updateAccount(accountId, userId, request.getName(), request.getType());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long userId, @PathVariable Long accountId) {
        accountUseCase.deleteAccount(accountId, userId);
        return ResponseEntity.noContent().build();
    }
}