package com.api.personal.finance.infrastructure.persistence.adapter;

import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.infrastructure.persistence.entity.UserJpaEntity;
import com.api.personal.finance.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    @Test
    @DisplayName("FindAll deve buscar todas as entidades e converter para domínio")
    void shouldFindAllUsers() {
        UserJpaEntity entity = UserJpaEntity.builder()
                .id(1L).name("João").email("joao@email.com").password("pass")
                .createdAt(Instant.now()).updatedAt(Instant.now()).build();

        when(userJpaRepository.findAll()).thenReturn(List.of(entity));

        List<User> result = userRepositoryAdapter.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("FindById deve retornar Optional do domínio")
    void shouldFindById() {
        UserJpaEntity entity = UserJpaEntity.builder()
                .id(1L).name("João").email("joao@email.com").password("pass")
                .createdAt(Instant.now()).updatedAt(Instant.now()).build();

        when(userJpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<User> result = userRepositoryAdapter.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("João");
    }

    @Test
    @DisplayName("FindByEmail deve retornar Optional do domínio")
    void shouldFindByEmail() {
        UserJpaEntity entity = UserJpaEntity.builder()
                .id(1L).name("João").email("joao@email.com").password("pass")
                .createdAt(Instant.now()).updatedAt(Instant.now()).build();

        when(userJpaRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(entity));

        Optional<User> result = userRepositoryAdapter.findByEmail("joao@email.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("Save deve persistir e retornar o domínio salvo")
    void shouldSaveUser() {
        User domain = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        UserJpaEntity entity = UserJpaEntity.builder()
                .id(1L).name("João").email("joao@email.com").password("pass")
                .createdAt(Instant.now()).updatedAt(Instant.now()).build();

        when(userJpaRepository.save(any(UserJpaEntity.class))).thenReturn(entity);

        User saved = userRepositoryAdapter.save(domain);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("DeleteById deve chamar deleção no repositório JPA")
    void shouldDeleteById() {
        userRepositoryAdapter.deleteById(1L);

        verify(userJpaRepository).deleteById(1L);
    }
}