package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.Enums.PillarEnum;
import com.ambientese.grupo5.Model.Enums.AnswersEnum;

public class FormRequest {
    private Long questionId;
    private Long questionNumber;
    private String questionDescription;
    private AnswersEnum userAnswer;
    private PillarEnum questionPillar;
    private Long formId;

    public FormRequest() {
    }

    public FormRequest(Long questionId, PillarEnum questionPillar, AnswersEnum userAnswer) {
        this.questionId = questionId;
        this.questionPillar = questionPillar;
        this.userAnswer = userAnswer;
    }
    
    public FormRequest(Long questionId, String questionDescription, AnswersEnum userAnswer, PillarEnum questionPillar,
            Long formId) {
        this.questionId = questionId;
        this.questionDescription = questionDescription;
        this.userAnswer = userAnswer;
        this.questionPillar = questionPillar;
        this.formId = formId;
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


    public Long getFormId() {
        return formId;
    }


    public void setFormId(Long formId) {
        this.formId = formId;
    }

    
}
