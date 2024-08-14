package com.ambientese.grupo5.Model;

import com.ambientese.grupo5.Model.Enums.RespostasEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "Resposta")
public class RespostaModel {

    @EmbeddedId
    private RespostaId id = new RespostaId();

    @ManyToOne
    @MapsId("formularioId")
    @JoinColumn(name = "formulario_id")
    @JsonBackReference
    private FormularioModel formulario;

    @ManyToOne
    @MapsId("perguntaId")
    @JoinColumn(name = "pergunta_id")
    private PerguntasModel pergunta;

    @Enumerated(EnumType.STRING)
    @Column(name = "resposta")
    private RespostasEnum resposta;

    public RespostaModel() {}

    public RespostaModel(RespostaId id, FormularioModel formulario, PerguntasModel pergunta, RespostasEnum resposta) {
        this.id = id;
        this.formulario = formulario;
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public RespostaModel(FormularioModel formulario, PerguntasModel pergunta, RespostasEnum resposta) {
        this.formulario = formulario;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.id = new RespostaId(formulario.getId(), pergunta.getId());
    }

    public RespostaId getId() {
        return id;
    }

    public void setId(RespostaId id) {
        this.id = id;
    }

    public FormularioModel getFormulario() {
        return formulario;
    }

    public void setFormulario(FormularioModel formulario) {
        this.formulario = formulario;
    }

    public PerguntasModel getPergunta() {
        return pergunta;
    }

    public void setPergunta(PerguntasModel pergunta) {
        this.pergunta = pergunta;
    }

    public RespostasEnum getResposta() {
        return resposta;
    }

    public void setResposta(RespostasEnum resposta) {
        this.resposta = resposta;
    }
}
