package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.Enums.EixoEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PerguntasRequest {
    @NotNull
    @NotBlank
    private String descricao;

    @NotNull
    @NotBlank
    private EixoEnum eixo;

    public @NotNull @NotBlank String getDescricao() {
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
}
