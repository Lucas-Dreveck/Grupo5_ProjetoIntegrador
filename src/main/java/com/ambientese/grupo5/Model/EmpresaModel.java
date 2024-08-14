package com.ambientese.grupo5.Model;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.ambientese.grupo5.Model.Enums.PorteEnum;
import jakarta.persistence.*;

@Entity
@Table (name = "Empresa")
public class EmpresaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String nomeFantasia;

    @NotNull
    @NotBlank
    private String nomeSolicitante;

    @Column(length = 15)
    @Pattern(regexp = "^[0-9]+$")
    @NotNull
    @NotBlank
    private String telefoneSolicitante;

    @NotNull
    @NotBlank
    private String razaoSocial;

    @Column(length = 14)
    @Pattern(regexp = "^[0-9]+$")
    @NotNull
    @NotBlank
    private String cnpj;

    @Column(length = 20)
    private String inscricaoSocial;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private EnderecoModel endereco;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @Column(length = 15)
    @Pattern(regexp = "^[0-9]+$")
    @NotNull
    @NotBlank
    private String telefoneEmpresas;

    @NotNull
    @NotBlank
    private String ramo;

    @NotNull
    @NotBlank
    private PorteEnum porteEmpresas;

    @Column(name = "ranking")
    private Integer ranking;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmpresaModel other = (EmpresaModel) obj;
        return Objects.equals(id, other.id);
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

    public void setEndereco(EnderecoModel enderecoModel) {
        this.endereco = enderecoModel;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
}
