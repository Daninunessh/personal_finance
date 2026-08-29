package com.api.personal.finance.infrastructure.persistence;

import com.api.personal.finance.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    void shouldReturnAllUsersMappedToDomain() {

        LocalDateTime createdAt = LocalDateTime.of(2026, 8, 28, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 8, 28, 11, 0);

        UserJpaEntity userJpaEntity1 = UserJpaEntity.builder()
                .id(1L)
                .name("João")
                .email("joao@email.com")
                .password("123456")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        UserJpaEntity userJpaEntity2 = UserJpaEntity.builder()
                .id(2L)
                .name("Maria")
                .email("maria@email.com")
                .password("654321")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        when(userJpaRepository.findAll()).thenReturn(List.of(userJpaEntity1, userJpaEntity2));
        List<User> result = userRepositoryImpl.findAll();
        assertNotNull(result); assertEquals(2, result.size());
        User user1 = result.get(0);
        assertEquals(1L, user1.getId());
        assertEquals("João", user1.getName());
        assertEquals("joao@email.com", user1.getEmail());
        assertEquals("123456", user1.getPassword());
        assertEquals(createdAt, user1.getCreatedAt());
        assertEquals(updatedAt, user1.getUpdatedAt());

        User user2 = result.get(1);
        assertEquals(2L, user2.getId());
        assertEquals("Maria", user2.getName());
        assertEquals("maria@email.com", user2.getEmail());
        assertEquals("654321", user2.getPassword());
        assertEquals(createdAt, user2.getCreatedAt());
        assertEquals(updatedAt, user2.getUpdatedAt());
        verify(userJpaRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoUsers() {

        when(userJpaRepository.findAll()) .thenReturn(List.of());
        List<User> result = userRepositoryImpl.findAll();
        assertNotNull(result); assertTrue(result.isEmpty());
        verify(userJpaRepository, times(1)).findAll();
    }
}