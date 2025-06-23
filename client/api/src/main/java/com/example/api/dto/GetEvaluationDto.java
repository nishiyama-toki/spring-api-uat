package com.example.api.dto;

public class GetEvaluationDto {

    private Float skill_score;
    private Float business_score;
    private Float team_score;
    private String comment;

    // --- Getter & Setter ---

    public Float getSkill_score() {
        return skill_score;
    }

    public void setSkill_score(Float skill_score) {
        this.skill_score = skill_score;
    }

    public Float getBusiness_score() {
        return business_score;
    }

    public void setBusiness_score(Float business_score) {
        this.business_score = business_score;
    }

    public Float getTeam_score() {
        return team_score;
    }

    public void setTeam_score(Float team_score) {
        this.team_score = team_score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
