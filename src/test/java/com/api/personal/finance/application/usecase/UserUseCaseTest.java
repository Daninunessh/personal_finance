package com.api.personal.finance.application.usecase;

import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.domain.exception.EmailAlreadyExistsException;
import com.api.personal.finance.domain.exception.InvalidPasswordException;
import com.api.personal.finance.domain.exception.UserNotFoundException;
import com.api.personal.finance.domain.repository.UserRepository;
import com.api.personal.finance.domain.security.PasswordEncoderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    void shouldReturnAllUsers() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userUseCase.getAllUsers();

        assertThat(result).hasSize(1).contains(user);
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnUserByIdWhenFound() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userUseCase.getUserById(1L);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userUseCase.getUserById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Usuário não encontrado para o ID: 1");
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userUseCase.createUser("João", "joao@email.com", "rawPassword");

        assertThat(result.getName()).isEqualTo("João");
        assertThat(result.getEmail()).isEqualTo("joao@email.com");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingWithExistingEmail() {
        User existingUser = new User(2L, "Maria", "joao@email.com", "pass", Instant.now(), Instant.now());
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userUseCase.createUser("João", "joao@email.com", "rawPassword"))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("O e-mail 'joao@email.com' já está cadastrado.");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdateUserWithSameEmail() {
        User existingUser = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updated = userUseCase.updateUser(1L, "João Alterado", "joao@email.com");

        assertThat(updated.getName()).isEqualTo("João Alterado");
        verify(userRepository).save(existingUser);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingToEmailBelongingToAnotherUser() {
        User user1 = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        User user2 = new User(2L, "Maria", "maria@email.com", "pass", Instant.now(), Instant.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(user2));

        assertThatThrownBy(() -> userUseCase.updateUser(1L, "João", "maria@email.com"))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        User user = new User(1L, "João", "joao@email.com", "encodedOldPass", Instant.now(), Instant.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        userUseCase.changePassword(1L, "oldPass", "newPass");

        assertThat(user.getPassword()).isEqualTo("encodedNewPass");
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenOldPasswordIsIncorrect() {
        User user = new User(1L, "João", "joao@email.com", "encodedOldPass", Instant.now(), Instant.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedOldPass")).thenReturn(false);

        assertThatThrownBy(() -> userUseCase.changePassword(1L, "wrongPass", "newPass"))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("A senha antiga fornecida está incorreta.");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        User user = new User(1L, "João", "joao@email.com", "pass", Instant.now(), Instant.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userUseCase.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}