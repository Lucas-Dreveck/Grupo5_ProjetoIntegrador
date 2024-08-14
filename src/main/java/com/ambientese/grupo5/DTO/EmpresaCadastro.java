package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.EnderecoModel;
import com.ambientese.grupo5.Model.Enums.PorteEnum;

public class EmpresaCadastro {
    private Long id;
    private String nomeFantasia;
    private String nomeSolicitante;
    private String telefoneSolicitante;
    private String razaoSocial;
    private String cnpj;
    private String inscricaoSocial;
    private EnderecoModel endereco;
    private String email;
    private String telefoneEmpresas;
    private String ramo;
    private PorteEnum porteEmpresas;
    private Integer ranking;
    private Boolean finishList;

    public EmpresaCadastro() {}

    public EmpresaCadastro(Long id, String nomeFantasia, String nomeSolicitante, String telefoneSolicitante, String razaoSocial, String cnpj, String inscricaoSocial, EnderecoModel endereco, String email, String telefoneEmpresas, String ramo, PorteEnum porteEmpresas, Integer ranking, Boolean finishList) {
        this.id = id;
        this.nomeFantasia = nomeFantasia;
        this.nomeSolicitante = nomeSolicitante;
        this.telefoneSolicitante = telefoneSolicitante;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.inscricaoSocial = inscricaoSocial;
        this.endereco = endereco;
        this.email = email;
        this.telefoneEmpresas = telefoneEmpresas;
        this.ramo = ramo;
        this.porteEmpresas = porteEmpresas;
        this.ranking = ranking;
        this.finishList = finishList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
    }

    public String getTelefoneSolicitante() {
        return telefoneSolicitante;
    }

    public void setTelefoneSolicitante(String telefoneSolicitante) {
        this.telefoneSolicitante = telefoneSolicitante;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoSocial() {
        return inscricaoSocial;
    }

    public void setInscricaoSocial(String inscricaoSocial) {
        this.inscricaoSocial = inscricaoSocial;
    }

    public EnderecoModel getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoModel endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoneEmpresas() {
        return telefoneEmpresas;
    }

    public void setTelefoneEmpresas(String telefoneEmpresas) {
        this.telefoneEmpresas = telefoneEmpresas;
    }

    public String getRamo() {
        return ramo;
    }

    public void setRamo(String ramo) {
        this.ramo = ramo;
    }

    public PorteEnum getPorteEmpresas() {
        return porteEmpresas;
    }

    public void setPorteEmpresas(PorteEnum porteEmpresas) {
        this.porteEmpresas = porteEmpresas;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Boolean getFinishList() {
        return finishList;
    }

    public void setFinishList(Boolean finishList) {
        this.finishList = finishList;
    }

    
}
