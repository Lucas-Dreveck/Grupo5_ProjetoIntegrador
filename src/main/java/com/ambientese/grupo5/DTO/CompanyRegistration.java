package com.ambientese.grupo5.dto;

import com.ambientese.grupo5.model.AddressModel;
import com.ambientese.grupo5.model.enums.SizeEnum;

public class CompanyRegistration {
    private Long id;
    private String tradeName;
    private String applicantsName;
    private String applicantsPhone;
    private String companyName;
    private String cnpj;
    private String socialInscription;
    private AddressModel address;
    private String email;
    private String companyPhone;
    private String segment;
    private SizeEnum companySize;
    private Integer ranking;
    private Boolean finishList;
    
    public CompanyRegistration() {
    }

    public CompanyRegistration(Long id, String tradeName, String applicantsName, String applicantsPhone, String companyName,
            String cnpj, String socialInscription, AddressModel address, String email, String companyPhone, String segment,
            SizeEnum companySize, Integer ranking, Boolean finishList) {
        this.id = id;
        this.tradeName = tradeName;
        this.applicantsName = applicantsName;
        this.applicantsPhone = applicantsPhone;
        this.companyName = companyName;
        this.cnpj = cnpj;
        this.socialInscription = socialInscription;
        this.address = address;
        this.email = email;
        this.companyPhone = companyPhone;
        this.segment = segment;
        this.companySize = companySize;
        this.ranking = ranking;
        this.finishList = finishList;
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
