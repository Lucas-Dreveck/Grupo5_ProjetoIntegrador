package com.ambientese.grupo5.Services.PerguntasService;

import com.ambientese.grupo5.Model.Enums.EixoEnum;
import com.ambientese.grupo5.Model.PerguntasModel;
import com.ambientese.grupo5.Repository.PerguntasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtualizarPerguntasService {

    @Autowired
    private PerguntasRepository perguntasRepository;

    public PerguntasModel atualizarPergunta(long id, String novaDescricao, EixoEnum novoEixo) {
        PerguntasModel perguntaExistente = perguntasRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Pergunta não encontrada"));

        perguntaExistente.setDescricao(novaDescricao);
        perguntaExistente.setEixo(novoEixo);

        return perguntasRepository.save(perguntaExistente);
    }
}
