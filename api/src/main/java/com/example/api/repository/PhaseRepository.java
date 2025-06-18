package com.example.api.repository;

import com.example.api.entity.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface PhaseRepository extends JpaRepository<Phase, Integer> {
    // 現在日付がフェーズ期間内のフェーズを取得
    Optional<Phase> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate start, LocalDate end);
}
