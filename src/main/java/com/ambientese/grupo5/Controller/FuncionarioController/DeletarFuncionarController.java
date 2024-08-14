package com.ambientese.grupo5.Controller.FuncionarioController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ambientese.grupo5.Services.FuncionarioService.DeletarFuncionarioService;

@RestController
@RequestMapping("/auth/Funcionario")
@Validated
public class DeletarFuncionarController {
    
    @Autowired
    private DeletarFuncionarioService deletarFuncionario;

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<?> deletarFuncionario(@PathVariable Long id) {
        deletarFuncionario.deleteFuncionario(id);
        return ResponseEntity.ok().build();
    }
}
