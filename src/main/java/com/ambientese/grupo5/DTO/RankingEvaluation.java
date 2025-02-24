package com.ambientese.grupo5.dto;

import com.ambientese.grupo5.model.enums.CertificateLevelEnum;
import com.ambientese.grupo5.model.enums.SizeEnum;

public class RankingEvaluation {
    private long id;
    private Integer ranking;
    private String companyName;
    private CertificateLevelEnum certificateLevel;
    private String imageUrl;
    private String segment;
    private SizeEnum size;
    private String city;
    private Integer finalScore;
    private Integer socialScore;
    private Integer enviornmentalScore;
    private Integer governmentScore;
    private Boolean finishList;

    public RankingEvaluation(long id, Integer ranking, String companyName, CertificateLevelEnum certificateLevel,
            String imageUrl, String segment, SizeEnum size, String city, Integer finalScore, Integer socialScore,
            Integer enviornmentalScore, Integer governmentScore, Boolean finishList) {
        this.id = id;
        this.ranking = ranking;
        this.companyName = companyName;
        this.certificateLevel = certificateLevel;
        this.imageUrl = imageUrl;
        this.segment = segment;
        this.size = size;
        this.city = city;
        this.finalScore = finalScore;
        this.socialScore = socialScore;
        this.enviornmentalScore = enviornmentalScore;
        this.governmentScore = governmentScore;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CertificateLevelEnum getCertificateLevel() {
        return certificateLevel;
    }

    public void setCertificateLevel(CertificateLevelEnum certificateLevel) {
        this.certificateLevel = certificateLevel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public SizeEnum getSize() {
        return size;
    }

    public void setSize(SizeEnum size) {
        this.size = size;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public Integer getSocialScore() {
        return socialScore;
    }

    public void setSocialScore(Integer socialScore) {
        this.socialScore = socialScore;
    }

    public Integer getEnviornmentalScore() {
        return enviornmentalScore;
    }

    public void setEnviornmentalScore(Integer enviornmentalScore) {
        this.enviornmentalScore = enviornmentalScore;
    }

    public Integer getGovernmentScore() {
        return governmentScore;
    }

    public void setGovernmentScore(Integer governmentScore) {
        this.governmentScore = governmentScore;
    }

    public Boolean getFinishList() {
        return finishList;
    }

    public void setFinishList(Boolean finishList) {
        this.finishList = finishList;
    }

    
    
}