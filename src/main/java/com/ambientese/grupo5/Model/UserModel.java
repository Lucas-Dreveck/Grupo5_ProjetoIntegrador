package com.ambientese.grupo5.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private Boolean isAdmin;

    @Column(name = "recovery_code")
    private String recoveryCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }
}
