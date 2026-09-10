package com.api.personal.finance.infrastructure.persistence.adapter;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.repository.AccountRepository;
import com.api.personal.finance.infrastructure.persistence.entity.AccountJpaEntity;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import com.api.personal.finance.infrastructure.persistence.mapper.AccountMapper;
import com.api.personal.finance.infrastructure.persistence.repository.AccountJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Account> findByUserId(Long userId) {
        return accountJpaRepository.findByUserId(userId)
                .stream()
                .map(AccountMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Account> findByIdAndUserId(Long id, Long userId) {
        return accountJpaRepository.findByIdAndUserId(id, userId).map(AccountMapper::toDomain);
    }

    @Override
    public Account save(Account account) {
        UserJpaEntity userRef = entityManager.getReference(UserJpaEntity.class, account.getUser().getId());
        AccountJpaEntity entity = AccountMapper.toEntity(account, userRef);
        return AccountMapper.toDomain(accountJpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        accountJpaRepository.deleteById(id);
    }
}