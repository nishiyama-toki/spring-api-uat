package com.example.api.dto;

public class TargetResponseDto {

<<<<<<< HEAD
    private Integer id;
    private String name;
    private String role;

    private GetEvaluationDto evaluation; // ← ★追加！

    // --- Getter & Setter ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
=======
    private Long id; // <-- IntegerからLongに変更済み
    private String name;
    private String role;

    private GetEvaluationDto evaluation;

    // --- Getter & Setter ---
    public Long getId() { // <-- GetterもLong
        return id;
    }

    public void setId(Long id) { // <-- SetterもLong
>>>>>>> mizukami
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
<<<<<<< HEAD
}
=======
}
>>>>>>> mizukami
