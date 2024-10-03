package com.ambientese.grupo5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ambientese.grupo5.Model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByLogin(String login);
}
