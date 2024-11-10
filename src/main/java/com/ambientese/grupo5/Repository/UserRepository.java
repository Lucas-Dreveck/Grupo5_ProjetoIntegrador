package com.ambientese.grupo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ambientese.grupo5.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByLogin(String login);
}
