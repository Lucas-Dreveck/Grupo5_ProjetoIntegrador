package com.ambientese.grupo5.Services.UsuarioService;

import com.ambientese.grupo5.Model.UsuarioModel;
import com.ambientese.grupo5.Repository.UsuarioRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioModel createUsuario(UsuarioModel usuarioModel) {
        usuarioModel.setPassword(BCrypt.hashpw(usuarioModel.getPassword(), BCrypt.gensalt()));
        return usuarioRepository.save(usuarioModel);
    }

}
