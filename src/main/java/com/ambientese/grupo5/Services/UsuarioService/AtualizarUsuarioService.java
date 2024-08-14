package com.ambientese.grupo5.Services.UsuarioService;

import com.ambientese.grupo5.Model.UsuarioModel;
import com.ambientese.grupo5.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AtualizarUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<UsuarioModel> updateUsuario(Long id, UsuarioModel usuarioModel) {
        Optional<UsuarioModel> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isPresent()) {
            usuarioModel.setId(id);
            return Optional.of(usuarioRepository.save(usuarioModel));
        } else {
            return Optional.empty();
        }
    }
}
