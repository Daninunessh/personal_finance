package com.api.personal.finance.domain.repository;

import com.api.personal.finance.domain.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    List<Account> findByUserId(Long userId);

    Optional<Account> findByIdAndUserId(Long id, Long userId);

    Account save(Account account);

    void deleteById(Long id);
}