package com.ambientese.grupo5.Model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class AnswerId implements Serializable {
    private Long formId;
    private Long questionId;

    public AnswerId() {}

    public AnswerId(Long formId, Long questionId) {
        this.formId = formId;
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerId that = (AnswerId) o;
        return Objects.equals(formId, that.formId) &&
                Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formId, questionId);
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }


}