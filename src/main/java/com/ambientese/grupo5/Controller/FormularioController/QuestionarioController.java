package com.ambientese.grupo5.Controller.FormularioController;

import com.ambientese.grupo5.DTO.FormularioRequest;
import com.ambientese.grupo5.DTO.QuestionarioResponse;
import com.ambientese.grupo5.Model.FormularioModel;
import com.ambientese.grupo5.Services.FormulariosService.BuscarPerguntasDoBancoService;
import com.ambientese.grupo5.Services.FormulariosService.ProcessarFormularioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestionarioController {

    @Autowired
    private BuscarPerguntasDoBancoService buscarPerguntasService;

    @Autowired
    private ProcessarFormularioService processarFormularioService;

    @GetMapping("/auth/haveAvaliacaoAtiva/{empresaId}")
    public boolean avaliacaoAtiva(@PathVariable() Long empresaId) {
        return processarFormularioService.haveAvaliacaoAtiva(empresaId);
    }

    @GetMapping("/auth/questionario/{isNewForm}")
    public QuestionarioResponse exibirQuestionario(@PathVariable() Boolean isNewForm, @RequestParam(required = false) Long empresaId) {
        return buscarPerguntasService.buscarPerguntasDoBanco(isNewForm, empresaId);
    }

    @PostMapping("/auth/processarRespostas")
    public FormularioModel processarRespostas(@RequestParam("empresa_id") Long empresa_id, @RequestParam("is_complete") Boolean isComplete, @RequestBody List<FormularioRequest> respostas) {
        return processarFormularioService.criarFormulario(empresa_id, respostas, isComplete);
    }
}
