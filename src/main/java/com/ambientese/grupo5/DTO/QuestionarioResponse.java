package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.PerguntasModel;
import java.util.List;

public class QuestionarioResponse {
    private List<PerguntasModel> perguntas;
    private List<FormularioRequest> formularioRequests;

    public QuestionarioResponse() {}

    public QuestionarioResponse(List<PerguntasModel> perguntas, List<FormularioRequest> formularioRequests) {
        this.perguntas = perguntas;
        this.formularioRequests = formularioRequests;
    }

    public List<PerguntasModel> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<PerguntasModel> perguntas) {
        this.perguntas = perguntas;
    }

    public List<FormularioRequest> getFormularioRequests() {
        return formularioRequests;
    }

    public void setFormularioRequests(List<FormularioRequest> formularioRequests) {
        this.formularioRequests = formularioRequests;
    }
}
