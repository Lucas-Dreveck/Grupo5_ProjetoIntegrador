package com.ambientese.grupo5.Controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ambientese.grupo5.Model.UserModel;
import com.ambientese.grupo5.Services.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth/User")
@Tag(name = "Usuario", description = "Endpoints para gerenciamento de usuários")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        Optional<UserModel> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserModel userModel) {
        UserModel createdUser = userService.createUser(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable Long id, @Valid @RequestBody UserModel userModel) {
        Optional<UserModel> updatedUser = userService.updateUser(id, userModel);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deletedUser = userService.deleteUser(id);
        return deletedUser ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
