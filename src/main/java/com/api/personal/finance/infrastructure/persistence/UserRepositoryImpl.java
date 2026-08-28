package com.api.personal.finance.infrastructure.persistence;

import com.api.personal.finance.application.repository.UserRepository;
import com.api.personal.finance.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public List<User> findAll() {

        return userJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private User toDomain(UserJpaEntity userJpaEntity) {

        return User.builder()
                .id(userJpaEntity.getId())
                .name(userJpaEntity.getName())
                .email(userJpaEntity.getEmail())
                .password(userJpaEntity.getPassword())
                .createdAt(userJpaEntity.getCreatedAt())
                .updatedAt(userJpaEntity.getUpdatedAt())
                .build();
    }
}
