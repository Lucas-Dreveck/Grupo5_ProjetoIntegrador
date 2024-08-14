package com.ambientese.grupo5.Controller.UsuarioController;

import com.ambientese.grupo5.Model.UsuarioModel;
import com.ambientese.grupo5.Services.UsuarioService.AtualizarUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth/Usuarios")
public class AtualizarUsuarioController {

    private final AtualizarUsuarioService usuarioService;

    public AtualizarUsuarioController(AtualizarUsuarioService usuarioService) {

        this.usuarioService = usuarioService;
    }

    @PutMapping("/Edit/{id}")
    public ResponseEntity<UsuarioModel> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioModel usuarioModel) {
        Optional<UsuarioModel> updatedUsuario = usuarioService.updateUsuario(id, usuarioModel);
        return updatedUsuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
