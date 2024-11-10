package com.ambientese.grupo5.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ambientese.grupo5.model.enums.PillarEnum;

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
