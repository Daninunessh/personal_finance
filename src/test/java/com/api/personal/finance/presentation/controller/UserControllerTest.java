package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.dto.UserResponse;
import com.api.personal.finance.application.usecase.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldReturnAllUsersSuccessfully() {
        UserResponse user1 = UserResponse.builder()
                .id(1L)
                .name("João")
                .email("joao@email.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        UserResponse user2 = UserResponse.builder()
                .id(2L)
                .name("Maria")
                .email("maria@email.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        List<UserResponse> users = List.of(user1, user2);
        when(userUseCase.getAll()).thenReturn(users);
        ResponseEntity<List<UserResponse>> response = userController.getAll();
        assertNotNull(response); assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(users, response.getBody());
        verify(userUseCase, times(1)).getAll();
    }

    @Test void shouldReturnEmptyListWhenThereAreNoUsers() {
        when(userUseCase.getAll()).thenReturn(List.of());
        ResponseEntity<List<UserResponse>> response = userController.getAll();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(userUseCase, times(1)).getAll();
    }
}