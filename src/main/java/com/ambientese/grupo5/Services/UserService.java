package com.ambientese.grupo5.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.ambientese.grupo5.model.UserModel;
import com.ambientese.grupo5.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel createUser(UserModel user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userRepository.save(user);
    }

    public Optional<UserModel> updateUser(Long id, UserModel user) {
        Optional<UserModel> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            return Optional.of(userRepository.save(user));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
