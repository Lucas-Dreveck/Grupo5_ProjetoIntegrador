package com.ambientese.grupo5.Model;

import com.ambientese.grupo5.Model.Enums.PillarEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Question")
public class QuestionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    @Enumerated(EnumType.STRING)
    private PillarEnum pillar;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AnswerModel> answers;

    public QuestionModel(String s, PillarEnum eixoEnum) {}

    public QuestionModel() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PillarEnum getPillar() {
        return pillar;
    }

    public void setPillar(PillarEnum pillar) {
        this.pillar = pillar;
    }

    public List<AnswerModel> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerModel> answers) {
        this.answers = answers;
    }


}
