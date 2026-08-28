package com.api.personal.finance.application.repository;

import com.api.personal.finance.domain.entity.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

}
