package com.ambientese.grupo5.model;

import com.ambientese.grupo5.model.enums.AnswersEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "Answer")
public class AnswerModel {

    @EmbeddedId
    private AnswerId id = new AnswerId();

    @ManyToOne
    @MapsId("evaluationId")
    @JoinColumn(name = "evaluation_id")
    @JsonBackReference
    private EvaluationModel evaluation;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private QuestionModel question;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer")
    private AnswersEnum answer;

    public AnswerModel() {}

    public AnswerModel(AnswerId id, EvaluationModel evaluation, QuestionModel question, AnswersEnum answer) {
        this.id = id;
        this.evaluation = evaluation;
        this.question = question;
        this.answer = answer;
    }

    public AnswerModel(EvaluationModel evaluation, QuestionModel question, AnswersEnum answer) {
        this.evaluation = evaluation;
        this.question = question;
        this.answer = answer;
        this.id = new AnswerId(evaluation.getId(), question.getId());
    }

    public AnswerId getId() {
        return id;
    }

    public void setId(AnswerId id) {
        this.id = id;
    }

    public EvaluationModel getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EvaluationModel evaluation) {
        this.evaluation = evaluation;
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
