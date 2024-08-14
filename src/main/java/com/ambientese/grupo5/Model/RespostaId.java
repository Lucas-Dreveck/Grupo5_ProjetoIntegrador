package com.ambientese.grupo5.Model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class RespostaId implements Serializable {
    private Long formularioId;
    private Long perguntaId;

    public RespostaId() {}

    public RespostaId(Long formularioId, Long perguntaId) {
        this.formularioId = formularioId;
        this.perguntaId = perguntaId;
    }

    public Long getFormularioId() {
        return formularioId;
    }

    public void setFormularioId(Long formularioId) {
        this.formularioId = formularioId;
    }

    public Long getPerguntaId() {
        return perguntaId;
    }

    public void setPerguntaId(Long perguntaId) {
        this.perguntaId = perguntaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RespostaId that = (RespostaId) o;
        return Objects.equals(formularioId, that.formularioId) &&
                Objects.equals(perguntaId, that.perguntaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formularioId, perguntaId);
    }
}
