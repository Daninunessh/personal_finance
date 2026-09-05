package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.usecase.UserUseCase;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.presentation.dto.request.UserChangePasswordRequest;
import com.api.personal.finance.presentation.dto.request.UserRequest;
import com.api.personal.finance.presentation.dto.request.UserUpdateRequest;
import com.api.personal.finance.presentation.dto.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("GetAllUsers deve retornar status 200 OK e lista de DTOs")
    void shouldGetAllUsers() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        when(userUseCase.getAllUsers()).thenReturn(List.of(user));

        ResponseEntity<List<UserResponse>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("GetUserById deve retornar status 200 OK com o usuário")
    void shouldGetUserById() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        when(userUseCase.getUserById(1L)).thenReturn(user);

        ResponseEntity<UserResponse> response = userController.getUserById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("CreateUser deve cadastrar e retornar status 201 Created com header Location")
    void shouldCreateUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        UserRequest userRequest = new UserRequest();
        userRequest.setName("João");
        userRequest.setEmail("joao@email.com");
        userRequest.setPassword("123456");

        User createdUser = new User(1L, "João", "joao@email.com", "encodedPass", Instant.now(), Instant.now());
        when(userUseCase.createUser("João", "joao@email.com", "123456")).thenReturn(createdUser);

        ResponseEntity<Void> response = userController.createUser(userRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).hasPath("/1");
    }

    @Test
    @DisplayName("UpdateUser deve retornar status 204 No Content")
    void shouldUpdateUser() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("João");
        request.setEmail("joao@email.com");

        ResponseEntity<Void> response = userController.updateUser(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userUseCase).updateUser(1L, "João", "joao@email.com");
    }

    @Test
    @DisplayName("ChangePassword deve retornar status 204 No Content")
    void shouldChangePassword() {
        UserChangePasswordRequest request = new UserChangePasswordRequest();
        request.setOldPassword("oldPass");
        request.setNewPassword("newPass");

        ResponseEntity<Void> response = userController.changePassword(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userUseCase).changePassword(1L, "oldPass", "newPass");
    }

    @Test
    @DisplayName("DeleteUser deve retornar status 204 No Content")
    void shouldDeleteUser() {
        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userUseCase).deleteUser(1L);
    }
}