package com.ambientese.grupo5.Controller.PerguntasController;

import com.ambientese.grupo5.DTO.PerguntasRequest;
import com.ambientese.grupo5.Model.PerguntasModel;
import com.ambientese.grupo5.Services.PerguntasService.AtualizarPerguntasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/Perguntas")
public class AtualizarPerguntasController {

    @Autowired
    private AtualizarPerguntasService atualizarPerguntasService;

    @PutMapping("/Edit/{id}")
    public PerguntasModel atualizarPergunta(@PathVariable long id, @RequestBody PerguntasRequest request) {
        return atualizarPerguntasService.atualizarPergunta(id, request.getDescricao(), request.getEixo());
    }
}
