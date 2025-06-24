package com.example.api.dto;

public class TargetResponseDto {

    private Integer id;
    private String name;
    private String role;

    private GetEvaluationDto evaluation; // ← ★追加！

    // --- Getter & Setter ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public GetEvaluationDto getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(GetEvaluationDto evaluation) {
        this.evaluation = evaluation;
    }
}
