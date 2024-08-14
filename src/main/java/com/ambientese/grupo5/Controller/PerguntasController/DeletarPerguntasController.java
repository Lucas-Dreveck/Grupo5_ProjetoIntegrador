package com.ambientese.grupo5.Controller.PerguntasController;

import com.ambientese.grupo5.Services.PerguntasService.DeletarPerguntasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/Perguntas")
public class DeletarPerguntasController {

    @Autowired
    private DeletarPerguntasService deletarPerguntasService;

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deletarPergunta(@PathVariable long id) {
        return deletarPerguntasService.deletarPergunta(id);
    }
}
