package com.ambientese.grupo5.Controller.FuncionarioController;

import com.ambientese.grupo5.DTO.FuncionarioRequest;
import com.ambientese.grupo5.Model.FuncionarioModel;
import com.ambientese.grupo5.Services.FuncionarioService.AtualizarFuncionarioService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth/Funcionario")
@Validated
public class AtualizarFuncionarioController {
    private final AtualizarFuncionarioService atualizarFuncionario;

    public AtualizarFuncionarioController(AtualizarFuncionarioService atualizarFuncionario) {
        this.atualizarFuncionario = atualizarFuncionario;
    }

    @PutMapping("/Edit/{id}")
    public FuncionarioModel atualizarFuncionario(@PathVariable Long id, @Valid @RequestBody FuncionarioRequest funcionarioRequest) {
        return atualizarFuncionario.atualizarFuncionario(id, funcionarioRequest);
    }
}
