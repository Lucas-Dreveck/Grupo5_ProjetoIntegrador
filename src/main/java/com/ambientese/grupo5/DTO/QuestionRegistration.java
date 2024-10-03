package com.ambientese.grupo5.DTO;

import com.ambientese.grupo5.Model.Enums.PillarEnum;

public class QuestionRegistration {
    private long id;
    private String description;
    private PillarEnum pillar;
    private Boolean finishList;

    public QuestionRegistration() {
    }

    public QuestionRegistration(long id, String description, PillarEnum pillar, Boolean finishList) {
        this.id = id;
        this.description = description;
        this.pillar = pillar;
        this.finishList = finishList;
    }

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

    public Boolean getFinishList() {
        return finishList;
    }

    public void setFinishList(Boolean finishList) {
        this.finishList = finishList;
    }


}
