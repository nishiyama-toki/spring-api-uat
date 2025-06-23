package com.example.api.repository;

import com.example.api.entity.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {

    /** 新規登録時の重複チェック */
    boolean existsByPhaseNumberAndName(Integer phaseNumber, String periodName);

    /** 編集時の重複チェック（自分以外） */
    boolean existsByPhaseNumberAndNameAndIdNot(Integer phaseNumber, String periodName, Long id);

    /** 今日が期間に含まれているフェーズを取得（ホーム画面・バッチ用） */
    List<Phase> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate today1, LocalDate today2);
}
