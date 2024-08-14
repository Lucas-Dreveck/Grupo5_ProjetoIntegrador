package com.ambientese.grupo5.Controller.PerguntasController;

import com.ambientese.grupo5.DTO.PerguntasRequest;
import com.ambientese.grupo5.Model.PerguntasModel;
import com.ambientese.grupo5.Services.PerguntasService.CriarPerguntasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/Perguntas")
public class CriarPerguntasController {

    @Autowired
    private CriarPerguntasService criarPerguntasService;

    @PostMapping("/Add")
    public PerguntasModel criarPergunta(@RequestBody PerguntasRequest request) {
        return criarPerguntasService.criarPergunta(request.getDescricao(), request.getEixo());
    }
}
