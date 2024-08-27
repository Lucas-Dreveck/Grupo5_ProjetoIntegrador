package com.ambientese.grupo5.Controller;

import java.util.List;

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

import com.ambientese.grupo5.DTO.PerguntaCadastro;
import com.ambientese.grupo5.DTO.PerguntasRequest;
import com.ambientese.grupo5.Model.PerguntasModel;
import com.ambientese.grupo5.Model.Enums.EixoEnum;
import com.ambientese.grupo5.Services.PerguntasService.AtualizarPerguntasService;
import com.ambientese.grupo5.Services.PerguntasService.CriarPerguntasService;
import com.ambientese.grupo5.Services.PerguntasService.DeletarPerguntasService;
import com.ambientese.grupo5.Services.PerguntasService.ListarPerguntasService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth/Perguntas")
@Tag(name = "Perguntas", description = "Endpoints para gerenciamento de perguntas")
public class PerguntasController {
   
    @Autowired
    private ListarPerguntasService listarPerguntasService;

    @Autowired
    private CriarPerguntasService criarPerguntasService;


    @Autowired
    private AtualizarPerguntasService atualizarPerguntasService;

    @Autowired
    private DeletarPerguntasService deletarPerguntasService;

    @GetMapping("/eixo/{eixo}")
    public List<PerguntasModel> listarPerguntasPorEixo(@PathVariable EixoEnum eixo) {
        return listarPerguntasService.listarPerguntasPorEixo(eixo);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PerguntaCadastro>> buscarPerguntas(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {


        List<PerguntaCadastro> resultado = listarPerguntasService.allPagedPerguntasWithFilter(nome, page, size);

        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/Add")
    public PerguntasModel criarPergunta(@RequestBody PerguntasRequest request) {
        return criarPerguntasService.criarPergunta(request.getDescricao(), request.getEixo());
    }

    @PutMapping("/Edit/{id}")
    public PerguntasModel atualizarPergunta(@PathVariable long id, @RequestBody PerguntasRequest request) {
        return atualizarPerguntasService.atualizarPergunta(id, request.getDescricao(), request.getEixo());
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deletarPergunta(@PathVariable long id) {
        return deletarPerguntasService.deletarPergunta(id);
    }
}
