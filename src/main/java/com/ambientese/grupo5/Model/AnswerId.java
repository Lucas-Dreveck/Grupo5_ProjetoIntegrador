package com.ambientese.grupo5.model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class AnswerId implements Serializable {
    private Long evaluationId;
    private Long questionId;

    public AnswerId() {}

    public AnswerId(Long evaluationId, Long questionId) {
        this.evaluationId = evaluationId;
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerId that = (AnswerId) o;
        return Objects.equals(evaluationId, that.evaluationId) &&
                Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaluationId, questionId);
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }


}