package com.api.personal.finance.infrastructure.persistence.adapter;

import com.api.personal.finance.domain.entity.Transaction;
import com.api.personal.finance.domain.repository.TransactionRepository;
import com.api.personal.finance.infrastructure.persistence.entity.AccountJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.CategoryJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.TransactionJpaEntity;
import com.api.personal.finance.infrastructure.persistence.mapper.TransactionMapper;
import com.api.personal.finance.infrastructure.persistence.repository.TransactionJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return transactionJpaRepository.findByUserId(userId)
                .stream()
                .map(TransactionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionJpaRepository
                .findByUserIdAndTransactionDateBetween(userId, startDate, endDate)
                .stream()
                .map(TransactionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Transaction> findByIdAndUserId(Long transactionId, Long userId) {
        return transactionJpaRepository.findByIdAndUserId(transactionId, userId).map(TransactionMapper::toDomain);
    }

    @Override
    public Transaction save(Transaction transaction) {
        AccountJpaEntity accountJpaEntity = entityManager.getReference(AccountJpaEntity.class, transaction.getAccount().getId());
        CategoryJpaEntity categoryJpaEntity = entityManager.getReference(CategoryJpaEntity.class, transaction.getCategory().getId());
        TransactionJpaEntity entity = TransactionMapper.toEntity(transaction, accountJpaEntity, categoryJpaEntity);

        return TransactionMapper.toDomain(transactionJpaRepository.save(entity));
    }

    @Override
    public void deleteByIdAndUserId(Long transactionId, Long userId) {
        transactionJpaRepository.deleteByIdAndUserId(transactionId, userId);
    }
}