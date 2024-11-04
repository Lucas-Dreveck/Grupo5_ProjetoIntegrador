package com.ambientese.grupo5.model;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.ambientese.grupo5.model.enums.SizeEnum;

import jakarta.persistence.*;

@Entity
@Table (name = "Company")
public class CompanyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String tradeName;

    @NotNull
    @NotBlank
    private String applicantsName;

    @Column(length = 15)
    @Pattern(regexp = "^[0-9]+$")
    @NotNull
    @NotBlank
    private String applicantsPhone;

    @NotNull
    @NotBlank
    private String companyName;

    @Column(length = 14)
    @Pattern(regexp = "^[0-9]+$")
    @NotNull
    @NotBlank
    private String cnpj;

    @Column(length = 20)
    private String socialInscription;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressModel addres;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @Column(length = 15)
    @Pattern(regexp = "^[0-9]+$")
    @NotNull
    @NotBlank
    private String companyPhone;

    @NotNull
    @NotBlank
    private String segment;

    @NotNull
    @NotBlank
    private SizeEnum companySize;

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
        CompanyModel other = (CompanyModel) obj;
        return Objects.equals(id, other.id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public AddressModel getAddres() {
        return addres;
    }

    public void setAddres(AddressModel addres) {
        this.addres = addres;
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

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    
}