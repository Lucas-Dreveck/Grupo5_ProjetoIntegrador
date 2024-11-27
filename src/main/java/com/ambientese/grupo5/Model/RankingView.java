package com.ambientese.grupo5.model;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import com.ambientese.grupo5.model.enums.CertificateLevelEnum;
import com.ambientese.grupo5.model.enums.SizeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Immutable
@Subselect(
    "SELECT " +
    "   c.id AS id, " +
    "   c.ranking AS ranking, " +
    "   c.trade_name AS company_name, " +
    "   e.certificate_level AS certificate_level, " +
    "   c.image_url AS image_url, " +
    "   c.segment AS segment, " +
    "   c.size AS size, " +
    "   a.city AS city, " +
    "   e.final_score AS final_score, " +
    "   e.social_score AS social_score, " +
    "   e.environmental_score AS environmental_score, " +
    "   e.government_score AS government_score, " +
    "   FALSE AS finish_list " +
    "FROM " +
    "   company c " +
    "JOIN " +
    "   evaluation e ON c.id = e.company_id " +
    "JOIN " +
    "   address a ON c.address_id = a.id"
)
public class RankingView {

    @Id
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
    private Integer environmentalScore;
    private Integer governmentScore;
    private Boolean finishList;

    public RankingView(long id, Integer ranking, String companyName, CertificateLevelEnum certificateLevel,
            String imageUrl, String segment, SizeEnum size, String city, Integer finalScore, Integer socialScore,
            Integer environmentalScore, Integer governmentScore, Boolean finishList) {
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
        this.environmentalScore = environmentalScore;
        this.governmentScore = governmentScore;
        this.finishList = finishList;
    }

    public long getId() { return id; }
    public Integer getRanking() { return ranking; }
    public String getCompanyName() { return companyName; }
    public CertificateLevelEnum getCertificateLevel() { return certificateLevel; }
    public String getImageUrl() { return imageUrl; }
    public String getSegment() { return segment; }
    public SizeEnum getSize() { return size; }
    public String getCity() { return city; }
    public Integer getFinalScore() { return finalScore; }
    public Integer getSocialScore() { return socialScore; }
    public Integer getEnvironmentalScore() { return environmentalScore; }
    public Integer getGovernmentScore() { return governmentScore; }
    public Boolean getFinishList() { return finishList; }
    
    
}