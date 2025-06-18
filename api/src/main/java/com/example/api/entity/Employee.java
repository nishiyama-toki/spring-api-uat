package com.example.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employees", schema = "evaluation")
public class Employee {

    @Id
    private Integer id;

    private String name;

    private String role;

// ----------------------------
// Getter & Setter
// ----------------------------

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
}
