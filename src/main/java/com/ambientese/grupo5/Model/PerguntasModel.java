package com.ambientese.grupo5.Model;

import com.ambientese.grupo5.Model.Enums.EixoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Pergunta")
public class PerguntasModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    private String descricao;

    @NotNull
    @NotBlank
    @Enumerated(EnumType.STRING)
    private EixoEnum eixo;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL)
    @JsonIgnore // Adicionando esta anotação para evitar a serialização da lista de respostas
    private List<RespostaModel> respostas;

    public PerguntasModel(String s, EixoEnum eixoEnum) {}

    public PerguntasModel() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EixoEnum getEixo() {
        return eixo;
    }

    public void setEixo(EixoEnum eixo) {
        this.eixo = eixo;
    }

    public List<RespostaModel> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<RespostaModel> respostas) {
        this.respostas = respostas;
    }
}
