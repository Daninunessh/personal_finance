package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.domain.entity.User;
import com.api.personal.finance.presentation.dto.request.UserChangePasswordRequest;
import com.api.personal.finance.presentation.dto.request.UserRequest;
import com.api.personal.finance.presentation.dto.request.UserUpdateRequest;
import com.api.personal.finance.presentation.dto.response.UserResponse;
import com.api.personal.finance.application.usecase.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponseList = userUseCase.getAllUsers()
                .stream()
                .map(UserResponse::fromDomain)
                .toList();
        return ResponseEntity.ok().body(userResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = UserResponse.fromDomain(userUseCase.getUserById(id));
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRequest userRequest) {
        User user = userUseCase.createUser(
                userRequest.getName(),
                userRequest.getEmail(),
                userRequest.getPassword()
        );
        UserResponse createdUser = UserResponse.fromDomain(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        userUseCase.updateUser(id, request.getName(), request.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody UserChangePasswordRequest request) {
        userUseCase.changePassword(id, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}