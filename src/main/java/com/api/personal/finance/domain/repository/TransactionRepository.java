package com.api.personal.finance.domain.repository;

import com.api.personal.finance.domain.entity.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    Transaction save(Transaction transaction);

    void deleteByIdAndUserId(Long id, Long userId);

}