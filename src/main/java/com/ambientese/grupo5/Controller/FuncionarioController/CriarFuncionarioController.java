package com.ambientese.grupo5.Controller.FuncionarioController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ambientese.grupo5.DTO.FuncionarioRequest;
import com.ambientese.grupo5.Model.FuncionarioModel;
import com.ambientese.grupo5.Services.FuncionarioService.CriarFuncionarioService;

@RestController
@RequestMapping("/auth/Funcionario")
@Validated
public class CriarFuncionarioController {
    

    @Autowired
    private CriarFuncionarioService criarFuncionario;

    @PostMapping("/Add")
    public FuncionarioModel criarFuncionario (@RequestBody FuncionarioRequest funcionarioModel) {
        return criarFuncionario.criarFuncionario(funcionarioModel);
    }
}
