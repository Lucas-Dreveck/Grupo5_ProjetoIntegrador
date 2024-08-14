package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.Enums.NivelCertificadoEnum;
import com.ambientese.grupo5.Model.Enums.PorteEnum;

public class FormularioRanking {
    private long id;
    private Integer ranking;
    private String empresaNome;
    private NivelCertificadoEnum nivelCertificado;
    private String ramo;
    private PorteEnum porte;
    private Integer pontuacaoFinal;
    private Integer pontuacaoSocial;
    private Integer pontuacaoAmbiental;
    private Integer pontuacaoGovernamental;
    private Boolean finishList;

    public FormularioRanking(long id, Integer ranking, String empresaNome, NivelCertificadoEnum nivelCertificado, String ramo, PorteEnum porte, Integer pontuacaoFinal,
                        Integer pontuacaoSocial,Integer pontuacaoAmbiental, Integer pontuacaoGovernamental, Boolean finishList) {
        this.id = id;
        this.ranking = ranking;
        this.empresaNome = empresaNome;
        this.nivelCertificado = nivelCertificado;
        this.ramo = ramo;
        this.porte = porte;
        this.pontuacaoFinal = pontuacaoFinal;
        this.pontuacaoSocial = pontuacaoSocial;
        this.pontuacaoAmbiental = pontuacaoAmbiental;
        this.pontuacaoGovernamental = pontuacaoGovernamental;
        this.finishList = finishList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public NivelCertificadoEnum getNivelCertificado() {
        return nivelCertificado;
    }

    public void setNivelCertificado(NivelCertificadoEnum nivelCertificado) {
        this.nivelCertificado = nivelCertificado;
    }

    public String getRamo() {
        return ramo;
    }

    public void setRamo(String ramo) {
        this.ramo = ramo;
    }

    public PorteEnum getPorte() {
        return porte;
    }

    public void setPorte(PorteEnum porte) {
        this.porte = porte;
    }

    public Integer getPontuacaoFinal() {
        return pontuacaoFinal;
    }

    public void setPontuacaoFinal(Integer pontuacaoFinal) {
        this.pontuacaoFinal = pontuacaoFinal;
    }

    public Integer getPontuacaoSocial() {
        return pontuacaoSocial;
    }

    public void setPontuacaoSocial(Integer pontuacaoSocial) {
        this.pontuacaoSocial = pontuacaoSocial;
    }

    public Integer getPontuacaoAmbiental() {
        return pontuacaoAmbiental;
    }

    public void setPontuacaoAmbiental(Integer pontuacaoAmbiental) {
        this.pontuacaoAmbiental = pontuacaoAmbiental;
    }

    public Integer getPontuacaoGovernamental() {
        return pontuacaoGovernamental;
    }

    public void setPontuacaoGovernamental(Integer pontuacaoGovernamental) {
        this.pontuacaoGovernamental = pontuacaoGovernamental;
    }

    public Boolean getFinishList() {
        return finishList;
    }

    public void setFinishList(Boolean finishList) {
        this.finishList = finishList;
    }
}
