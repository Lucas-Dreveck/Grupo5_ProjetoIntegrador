package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.QuestionModel;
import java.util.List;

public class EvaluationResponse {
    private List<QuestionModel> questions;
    private List<EvaluationRequest> evaluationRequests;
    
    public EvaluationResponse() {
    }

    public EvaluationResponse(List<QuestionModel> questions, List<EvaluationRequest> evaluationRequests) {
        this.questions = questions;
        this.evaluationRequests = evaluationRequests;
    }

    public List<QuestionModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionModel> questions) {
        this.questions = questions;
    }

    public List<EvaluationRequest> getEvaluationRequests() {
        return evaluationRequests;
    }

    public void setEvaluationRequests(List<EvaluationRequest> evaluationRequests) {
        this.evaluationRequests = evaluationRequests;
    }

    
}
