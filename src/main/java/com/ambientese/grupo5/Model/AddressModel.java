package com.ambientese.grupo5.Model;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Address")
public class AddressModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String cep;

    @NotNull
    private Integer number;

    @NotNull
    @NotBlank
    private String patio;

    private String complement;

    @NotNull
    @NotBlank
    private String city;

    @NotNull
    @NotBlank
    private String neighborhood;

    @NotNull
    @NotBlank
    private String UF;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPatio() {
        return patio;
    }

    public void setPatio(String patio) {
        this.patio = patio;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String uF) {
        UF = uF;
    }


}