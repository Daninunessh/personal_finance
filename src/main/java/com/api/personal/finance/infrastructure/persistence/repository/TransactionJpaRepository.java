package com.api.personal.finance.infrastructure.persistence.repository;

import com.api.personal.finance.infrastructure.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, Long> {

    @Query("""
        SELECT t
        FROM TransactionJpaEntity t
        JOIN t.account a
        WHERE a.user.id = :userId
        ORDER BY t.transactionDate DESC
    """)
    List<TransactionJpaEntity> findByUserId(
            @Param("userId") Long userId
    );

    @Query("""
    SELECT t
    FROM TransactionJpaEntity t
    JOIN t.account a
    WHERE a.user.id = :userId
      AND t.transactionDate BETWEEN :startDate AND :endDate
    ORDER BY t.transactionDate DESC
""")
    List<TransactionJpaEntity> findByUserIdAndTransactionDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT t
        FROM TransactionJpaEntity t
        JOIN t.account a
        WHERE t.id = :id
          AND a.user.id = :userId
    """)
    Optional<TransactionJpaEntity> findByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );

    @Modifying
    @Query("""
        DELETE FROM TransactionJpaEntity t
        WHERE t.id = :id
          AND t.account.user.id = :userId
    """)
    void deleteByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId
    );
}