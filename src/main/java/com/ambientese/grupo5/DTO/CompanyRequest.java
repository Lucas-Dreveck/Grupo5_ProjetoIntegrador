package com.ambientese.grupo5.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.ambientese.grupo5.model.AddressModel;
import com.ambientese.grupo5.model.enums.SizeEnum;

public class CompanyRequest {

    @NotNull
    @NotBlank
    private String tradeName;

    @NotNull
    @NotBlank
    private String applicantsName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]+$")
    private String applicantsPhone;

    @NotNull
    @NotBlank
    private String companyName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]+$")
    private String cnpj;

    @Column(length = 20)
    private String socialInscription;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressModel address;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]+$")
    private String companyPhone;

    @NotNull
    @NotBlank
    private String segment;

    @NotNull
    @NotBlank
    private SizeEnum companySize;

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getApplicantsName() {
        return applicantsName;
    }

    public void setApplicantsName(String applicantsName) {
        this.applicantsName = applicantsName;
    }

    public String getApplicantsPhone() {
        return applicantsPhone;
    }

    public void setApplicantsPhone(String applicantsPhone) {
        this.applicantsPhone = applicantsPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getSocialInscription() {
        return socialInscription;
    }

    public void setSocialInscription(String socialInscription) {
        this.socialInscription = socialInscription;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public SizeEnum getCompanySize() {
        return companySize;
    }

    public void setCompanySize(SizeEnum companySize) {
        this.companySize = companySize;
    }


    
}
