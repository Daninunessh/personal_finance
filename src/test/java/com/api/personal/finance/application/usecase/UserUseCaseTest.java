package com.api.personal.finance.application.usecase;

import com.api.personal.finance.application.dto.UserResponse;
import com.api.personal.finance.application.repository.UserRepository;
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
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    void shouldReturnAllUsers() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 8, 28, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 8, 28, 11, 0);

        User user1 = User.builder()
                .id(1L)
                .name("João")
                .email("joao@email.com")
                .password("123456")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("Maria")
                .email("maria@email.com")
                .password("654321")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<UserResponse> result = userUseCase.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("João", result.get(0).getName());
        assertEquals("joao@email.com", result.get(0).getEmail());
        assertEquals(createdAt, result.get(0).getCreatedAt());
        assertEquals(updatedAt, result.get(0).getUpdatedAt());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Maria", result.get(1).getName());
        assertEquals("maria@email.com", result.get(1).getEmail());
        assertEquals(createdAt, result.get(1).getCreatedAt());
        assertEquals(updatedAt, result.get(1).getUpdatedAt());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());
        List<UserResponse> result = userUseCase.getAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }
}