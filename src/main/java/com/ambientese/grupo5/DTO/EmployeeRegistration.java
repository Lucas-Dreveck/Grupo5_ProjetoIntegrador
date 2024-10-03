package com.ambientese.grupo5.DTO;

import java.time.LocalDate;

import com.ambientese.grupo5.Model.RoleModel;
import com.ambientese.grupo5.Model.UserModel;

public class EmployeeRegistration {
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private LocalDate birthDate;
    private RoleModel role;
    private UserModel user;
    private Boolean finishList;
   
    public EmployeeRegistration() {
    }

    public EmployeeRegistration(Long id, String name, String cpf, String email, LocalDate birthDate, RoleModel role,
            UserModel user, Boolean finishList) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.birthDate = birthDate;
        this.role = role;
        this.user = user;
        this.finishList = finishList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Boolean getFinishList() {
        return finishList;
    }

    public void setFinishList(Boolean finishList) {
        this.finishList = finishList;
    }

   
}
