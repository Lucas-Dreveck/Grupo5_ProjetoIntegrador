package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.Enums.EixoEnum;

public class PerguntaCadastro {
    private long id;
    private String descricao;
    private EixoEnum eixo;
    private Boolean finishList;

    public PerguntaCadastro() {
    }

    public PerguntaCadastro(long id, String descricao, EixoEnum eixo, Boolean finishList) {
        this.id = id;
        this.descricao = descricao;
        this.eixo = eixo;
        this.finishList = finishList;
    }

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

    public Boolean getFinishList() {
        return finishList;
    }

    public void setFinishList(Boolean finishList) {
        this.finishList = finishList;
    }

}
