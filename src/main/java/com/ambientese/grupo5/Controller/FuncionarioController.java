package com.ambientese.grupo5.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ambientese.grupo5.DTO.FuncionarioCadastro;
import com.ambientese.grupo5.DTO.FuncionarioRequest;
import com.ambientese.grupo5.Model.FuncionarioModel;
import com.ambientese.grupo5.Services.FuncionarioService.AtualizarFuncionarioService;
import com.ambientese.grupo5.Services.FuncionarioService.CriarFuncionarioService;
import com.ambientese.grupo5.Services.FuncionarioService.DeletarFuncionarioService;
import com.ambientese.grupo5.Services.FuncionarioService.ListarFuncionarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth/Funcionario")
@Tag(name = "Funcionario", description = "Endpoints para gerenciamento de funcionários")
public class FuncionarioController {

    @Autowired
    private ListarFuncionarioService listarFuncionarioService;

    @Autowired
    private CriarFuncionarioService criarFuncionario;

    @Autowired
    private AtualizarFuncionarioService atualizarFuncionarioService;

    @Autowired
    private DeletarFuncionarioService deletarFuncionarioService;

    @GetMapping("/search")
    public ResponseEntity<List<FuncionarioCadastro>> buscarFuncionarios(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<FuncionarioCadastro> resultado = listarFuncionarioService.allPagedFuncionariosWithFilter(nome, page, size);

        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/Add")
    public FuncionarioModel criarFuncionario (@RequestBody FuncionarioRequest funcionarioModel) {
        return criarFuncionario.criarFuncionario(funcionarioModel);
    }

    @PutMapping("/Edit/{id}")
    public FuncionarioModel atualizarFuncionario(@PathVariable Long id, @Valid @RequestBody FuncionarioRequest funcionarioRequest) {
        return atualizarFuncionarioService.atualizarFuncionario(id, funcionarioRequest);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<?> deletarFuncionario(@PathVariable Long id) {
        deletarFuncionarioService.deleteFuncionario(id);
        return ResponseEntity.ok().build();
    }
}
