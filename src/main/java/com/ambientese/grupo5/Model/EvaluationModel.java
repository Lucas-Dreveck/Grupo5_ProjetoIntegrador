package com.ambientese.grupo5.model;

import com.ambientese.grupo5.model.enums.CertificateLevelEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Evaluation")
public class EvaluationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<AnswerModel> answers = new ArrayList<>();

    @Column(name = "certificate")
    @Enumerated(EnumType.STRING)
    private CertificateLevelEnum certificate;

    @ManyToOne()
    @JoinColumn(name = "company_id")
    private CompanyModel company;

    @Column(name = "final_score")
    private Integer finalScore;

    @Column(name = "social_score")
    private Integer socialScore;

    @Column(name = "environmental_score")
    private Integer environmentalScore;

    @Column(name = "government_score")
    private Integer governmentScore;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "answer_date")
    private Date answerDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CertificateLevelEnum getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateLevelEnum certificate) {
        this.certificate = certificate;
    }

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
        this.company = company;
    }

    public void addAnswer(AnswerModel answer) {
        answers.add(answer);
        answer.setEvaluation(this);
    }

    public void removeAnswer(AnswerModel answer) {
        answers.remove(answer);
        answer.setEvaluation(null);
    }

    public List<AnswerModel> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerModel> answers) {
        this.answers.clear();
        if (answers != null) {
            for (AnswerModel answer : answers) {
                addAnswer(answer);
            }
        }
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

    public Integer getEnvironmentalScore() {
        return environmentalScore;
    }

    public void setEnvironmentalScore(Integer environmentalScore) {
        this.environmentalScore = environmentalScore;
    }

    public Integer getGovernmentScore() {
        return governmentScore;
    }

    public void setGovernmentScore(Integer governmentScore) {
        this.governmentScore = governmentScore;
    }

    public Date getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Date answerDate) {
        this.answerDate = answerDate;
    }


}