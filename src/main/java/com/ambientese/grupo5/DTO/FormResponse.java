package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.QuestionModel;
import java.util.List;

public class FormResponse {
    private List<QuestionModel> questions;
    private List<FormRequest> formRequests;
    
    public FormResponse() {
    }

    public FormResponse(List<QuestionModel> questions, List<FormRequest> formRequests) {
        this.questions = questions;
        this.formRequests = formRequests;
    }

    public List<QuestionModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionModel> questions) {
        this.questions = questions;
    }

    public List<FormRequest> getFormRequests() {
        return formRequests;
    }

    public void setFormRequests(List<FormRequest> formRequests) {
        this.formRequests = formRequests;
    }

    
}
