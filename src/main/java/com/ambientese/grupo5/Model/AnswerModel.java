package com.ambientese.grupo5.Model;

import com.ambientese.grupo5.Model.Enums.AnswersEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "Answer")
public class AnswerModel {

    @EmbeddedId
    private AnswerId id = new AnswerId();

    @ManyToOne
    @MapsId("formId")
    @JoinColumn(name = "form_id")
    @JsonBackReference
    private FormModel form;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private QuestionModel question;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer")
    private AnswersEnum answer;

    public AnswerModel() {}

    public AnswerModel(AnswerId id, FormModel form, QuestionModel question, AnswersEnum answer) {
        this.id = id;
        this.form = form;
        this.question = question;
        this.answer = answer;
    }

    public AnswerModel(FormModel form, QuestionModel question, AnswersEnum answer) {
        this.form = form;
        this.question = question;
        this.answer = answer;
        this.id = new AnswerId(form.getId(), question.getId());
    }

    public AnswerId getId() {
        return id;
    }

    public void setId(AnswerId id) {
        this.id = id;
    }

    public FormModel getForm() {
        return form;
    }

    public void setForm(FormModel form) {
        this.form = form;
    }

    public QuestionModel getQuestion() {
        return question;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }

    public AnswersEnum getAnswer() {
        return answer;
    }

    public void setAnswer(AnswersEnum answer) {
        this.answer = answer;
    }

    
}
