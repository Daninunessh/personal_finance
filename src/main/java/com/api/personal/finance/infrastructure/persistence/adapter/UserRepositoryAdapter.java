package com.api.personal.finance.infrastructure.persistence.adapter;

import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.domain.repository.UserRepository;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import com.api.personal.finance.infrastructure.persistence.repository.UserJpaRepository;
import com.api.personal.finance.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserMapper.toEntity(user);
        return UserMapper.toDomain(userJpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }
}
