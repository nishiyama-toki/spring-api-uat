package com.example.api.repository;

import com.example.api.entity.Phase;
import org.springframework.data.jpa.repository.JpaRepository;

// Phaseエンティティの主キーの型に合わせて、Longを指定します
public interface PhaseRepository extends JpaRepository<Phase, Long> {
}
