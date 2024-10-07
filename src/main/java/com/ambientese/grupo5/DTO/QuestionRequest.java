package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.Enums.PillarEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class QuestionRequest {
    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private PillarEnum pillar;

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

    
}
