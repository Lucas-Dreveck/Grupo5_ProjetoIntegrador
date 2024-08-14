package com.ambientese.grupo5.Services.PerguntasService;

import com.ambientese.grupo5.DTO.PerguntaCadastro;
import com.ambientese.grupo5.Model.PerguntasModel;
import com.ambientese.grupo5.Model.Enums.EixoEnum;
import com.ambientese.grupo5.Repository.PerguntasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarPerguntasService {

    @Autowired
    private PerguntasRepository perguntasRepository;

    public List<PerguntasModel> listarPerguntas() {
        return perguntasRepository.findAll();
    }

    public List<PerguntasModel> listarPerguntasPorEixo(EixoEnum eixo) {
        return perguntasRepository.findByEixo(eixo);
    }

    public List<PerguntaCadastro> allPagedPerguntasWithFilter(String nome, int page, int size) {
        List<PerguntasModel> perguntas;
        if (nome != null && !nome.isEmpty()) {
            perguntas = perguntasRepository.findAllByDescricaoStartingWithIgnoreCaseOrderByDescricaoAsc(nome);

            if (perguntas.isEmpty()) {
                perguntas = perguntasRepository.findAllOrderByDescricaoAsc().stream()
                        .filter(pergunta -> pergunta.getEixo().toString().toLowerCase().startsWith(nome.toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else {
            perguntas = perguntasRepository.findAll();
        }

        List<PerguntaCadastro> resultado = paginarPerguntas(perguntas, page, size);

        return resultado;
    }

    private List<PerguntaCadastro> paginarPerguntas(List<PerguntasModel> perguntas, int page, int size) {
        int total = perguntas.size();
        int start = Math.min(page * size, total);
        int end = Math.min((page + 1) * size, total);

        return perguntas.subList(start, end).stream()
                .map(pergunta -> new PerguntaCadastro(
                        pergunta.getId(),
                        pergunta.getDescricao(),
                        pergunta.getEixo(),
                        end == total
                ))
                .collect(Collectors.toList());
    }

}
