package com.ambientese.grupo5.Services.PerguntasService;

import com.ambientese.grupo5.Model.PerguntasModel;
import com.ambientese.grupo5.Repository.PerguntasRepository;
import com.ambientese.grupo5.Repository.RespostaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeletarPerguntasService {

    @Autowired
    private PerguntasRepository perguntasRepository;

    @Autowired
    private RespostaRepository respostaRepository;

    public ResponseEntity<String> deletarPergunta(long id) {
        PerguntasModel pergunta = perguntasRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("Pergunta não encontrada"));

        if (respostaRepository.existsByPerguntaId(pergunta.getId())) {
            return ResponseEntity.badRequest().body("Erro ao deletar, a pergunta está sendo utilizadas em avaliações.");
        } else {
            perguntasRepository.delete(pergunta);
            return ResponseEntity.noContent().build();
        }
    }
}
