package com.ambientese.grupo5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ambientese.grupo5.Model.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    UsuarioModel findByLogin(String login);
}
