package com.ambientese.grupo5.DTO;

import java.time.LocalDate;

import com.ambientese.grupo5.Model.CargoModel;
import com.ambientese.grupo5.Model.UsuarioModel;

public class FuncionarioCadastro {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
    private CargoModel cargo;
    private UsuarioModel usuario;
    private Boolean finishList;

    public FuncionarioCadastro() {
    }

    public FuncionarioCadastro(Long id, String nome, String cpf, String email, LocalDate dataNascimento, CargoModel cargo, UsuarioModel usuario, Boolean finishList) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.cargo = cargo;
        this.usuario = usuario;
        this.finishList = finishList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public CargoModel getCargo() {
        return cargo;
    }

    public void setCargo(CargoModel cargo) {
        this.cargo = cargo;
    }

    public Boolean getFinishList() {
        return finishList;
    }

    public void setFinishList(Boolean finishList) {
        this.finishList = finishList;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }
    
}
