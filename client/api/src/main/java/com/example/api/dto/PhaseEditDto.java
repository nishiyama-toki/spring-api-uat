package com.example.api.phase.dto;

import java.time.LocalDate;

public class PhaseEditDto extends PhaseDto {//編集は処理自体は同じなため継承を使用

    private Long id;

    // --- Getter & Setter ---
    //継承しているためゲッターとセッターはIDのみでかまわない 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}