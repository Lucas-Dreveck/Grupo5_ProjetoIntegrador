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

import com.ambientese.grupo5.Model.UsuarioModel;
import com.ambientese.grupo5.Services.UsuarioService.AtualizarUsuarioService;
import com.ambientese.grupo5.Services.UsuarioService.CriarUsuarioService;
import com.ambientese.grupo5.Services.UsuarioService.DeletarUsuarioService;
import com.ambientese.grupo5.Services.UsuarioService.ListarUsuarioService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth/Usuario")
@Tag(name = "Usuario", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {
    
    @Autowired
    private ListarUsuarioService listarUsuarioService;

    @Autowired
    private CriarUsuarioService criarUsuarioService;

    @Autowired
    private AtualizarUsuarioService atualizarUsuarioService;
    
    @Autowired
    private DeletarUsuarioService deletarUsuarioService;

    @GetMapping("/search")
    public ResponseEntity<List<UsuarioModel>> getAllUsuarios() {
        List<UsuarioModel> usuarioModels = listarUsuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarioModels);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<UsuarioModel> getUsuarioById(@PathVariable Long id) {
        Optional<UsuarioModel> usuario = listarUsuarioService.getUsuarioById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/Add")
    public ResponseEntity<UsuarioModel> createUsuario(@Valid @RequestBody UsuarioModel usuarioModel) {
        UsuarioModel createdUsuarioModel = criarUsuarioService.createUsuario(usuarioModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuarioModel);
    }

    @PutMapping("/Edit/{id}")
    public ResponseEntity<UsuarioModel> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioModel usuarioModel) {
        Optional<UsuarioModel> updatedUsuario = atualizarUsuarioService.updateUsuario(id, usuarioModel);
        return updatedUsuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        boolean deleted = deletarUsuarioService.deleteUsuario(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
