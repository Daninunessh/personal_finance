package com.api.personal.finance.application.usecase;

import com.api.personal.finance.domain.exception.EmailAlreadyExistsException;
import com.api.personal.finance.domain.exception.InvalidPasswordException;
import com.api.personal.finance.domain.exception.UserNotFoundException;
import com.api.personal.finance.domain.repository.UserRepository;
import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.domain.security.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createUser(String name, String email, String rawPassword) {
        checkEmailAvailability(email, null);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Instant now = Instant.now();
        User newUser = User.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return userRepository.save(newUser);
    }

    public User updateUser(Long id, String name, String email) {
        User existingUser = getUserById(id);
        checkEmailAvailability(email, id);
        existingUser.updateInfo(name, email, Instant.now());
        return userRepository.save(existingUser);
    }

    public void changePassword(Long id, String oldPassword, String newPassword) {
        User existingUser = getUserById(id);
        if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            throw new InvalidPasswordException("A senha antiga fornecida está incorreta.");
        }
        existingUser.updatePassword(passwordEncoder.encode(newPassword), Instant.now());
        userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User existingUser = getUserById(id);
        userRepository.deleteById(existingUser.getId());
    }

    private void checkEmailAvailability(String email, Long currentUserId) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (currentUserId == null || !user.getId().equals(currentUserId)) {
                throw new EmailAlreadyExistsException(email);
            }
        });
    }
}