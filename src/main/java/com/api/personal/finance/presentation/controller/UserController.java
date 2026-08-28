package com.api.personal.finance.presentation.controller;

import com.api.personal.finance.application.dto.UserResponse;
import com.api.personal.finance.application.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> userResponseList = userUseCase.getAll();
        return ResponseEntity.ok().body(userResponseList);
    }
}
