package com.example.api.phase.repository;

import com.example.api.phase.entity.Phase;
import org.springframework.data.jpa.repository.JpaRepository;//DBへの操作を容易にするためのクラス
import org.springframework.stereotype.Repository;//レポジトリであることの宣言

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {
    
    // フェーズ番号とクォーター名の組み合わせが既に存在するかチェック
    boolean existsByPhaseNumberAndPeriodName(Integer phaseNumber, String periodName);

    // 編集用に、指定したID以外で同じ phaseNumber & periodName のデータが存在するかチェック
    boolean existsByPhaseNumberAndPeriodNameAndIdNot(Integer phaseNumber, String periodName, Long id);

}
