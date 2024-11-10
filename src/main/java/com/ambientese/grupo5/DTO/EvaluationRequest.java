package com.ambientese.grupo5.dto;

import com.ambientese.grupo5.model.enums.AnswersEnum;
import com.ambientese.grupo5.model.enums.PillarEnum;

public class EvaluationRequest {
    private Long questionId;
    private Long questionNumber;
    private String questionDescription;
    private AnswersEnum userAnswer;
    private PillarEnum questionPillar;
    private Long evaluationId;

    public EvaluationRequest() {
    }

    public EvaluationRequest(Long questionId, PillarEnum questionPillar, AnswersEnum userAnswer) {
        this.questionId = questionId;
        this.questionPillar = questionPillar;
        this.userAnswer = userAnswer;
    }
    
    public EvaluationRequest(Long questionId, String questionDescription, AnswersEnum userAnswer, PillarEnum questionPillar,
            Long evaluationId) {
        this.questionId = questionId;
        this.questionDescription = questionDescription;
        this.userAnswer = userAnswer;
        this.questionPillar = questionPillar;
        this.evaluationId = evaluationId;
    }

    public Long getQuestionId() {
        return questionId;
    }


    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }


    public Long getQuestionNumber() {
        return questionNumber;
    }


    public void setQuestionNumber(Long questionNumber) {
        this.questionNumber = questionNumber;
    }


    public String getQuestionDescription() {
        return questionDescription;
    }


    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }


    public AnswersEnum getUserAnswer() {
        return userAnswer;
    }


    public void setUserAnswer(AnswersEnum userAnswer) {
        this.userAnswer = userAnswer;
    }


    public PillarEnum getQuestionPillar() {
        return questionPillar;
    }


    public void setQuestionPillar(PillarEnum questionPillar) {
        this.questionPillar = questionPillar;
    }


    public Long getEvaluationId() {
        return evaluationId;
    }


    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    
}
