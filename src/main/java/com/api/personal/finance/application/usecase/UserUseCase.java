package com.api.personal.finance.application.usecase;

import com.api.personal.finance.application.dto.UserResponse;
import com.api.personal.finance.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public List<UserResponse> getAll() {

        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }
}
