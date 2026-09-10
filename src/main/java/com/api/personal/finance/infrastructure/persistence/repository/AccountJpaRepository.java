package com.api.personal.finance.infrastructure.persistence.repository;

import com.api.personal.finance.infrastructure.persistence.entity.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, Long> {

    List<AccountJpaEntity> findByUserId(Long userId);

    Optional<AccountJpaEntity> findByIdAndUserId(Long id, Long userId);
}