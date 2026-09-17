package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.usecase.TransactionUseCase;
import com.api.personal.finance.domain.entity.Transaction;
import com.api.personal.finance.presentation.dto.request.TransactionRequest;
import com.api.personal.finance.presentation.dto.response.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionUseCase transactionUseCase;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long userId, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Transaction> transactionList;
        if (startDate != null && endDate != null) {
            transactionList = transactionUseCase.getTransactionsByUserIdAndTransactionDateBetween(userId, startDate, endDate);
        } else if (startDate == null && endDate == null) {
            transactionList = transactionUseCase.getTransactionsByUserId(userId);
        } else {
            throw new IllegalArgumentException("Informe startDate e endDate juntos.");
        }

        List<TransactionResponse> transactions = transactionList.stream()
                .map(TransactionResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionByIdAndUserId(@PathVariable Long userId, @PathVariable Long transactionId) {
        Transaction transaction = transactionUseCase.getTransactionByIdAndUserId(transactionId, userId);
        return ResponseEntity.ok().body(TransactionResponse.fromDomain(transaction));
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@PathVariable Long userId, @Valid @RequestBody TransactionRequest request) {
        Transaction createdTransaction = transactionUseCase.createTransaction(userId, request.getDescription(), request.getAmount(), request.getTransactionDate(), request.getAccountId(), request.getCategoryId());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{transactionId}").buildAndExpand(createdTransaction.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Void> updateTransaction(@PathVariable Long userId, @PathVariable Long transactionId, @Valid @RequestBody TransactionRequest request) {
        transactionUseCase.updateTransaction(transactionId, userId, request.getDescription(), request.getAmount(), request.getTransactionDate(), request.getAccountId(), request.getCategoryId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long userId, @PathVariable Long transactionId) {
        transactionUseCase.deleteTransaction(transactionId, userId);
        return ResponseEntity.noContent().build();
    }
}