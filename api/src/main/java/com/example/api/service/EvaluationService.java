
package com.example.api.service; // サービス用クラスをまとめるパッケージ

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.example.api.dto.EvaluationResponseDTO; // クライアント側に返す評価情報DTO
import com.example.api.repository.EvaluationRepository; // リポジトリ（DBアクセス用）

@Service // このクラスがサービス層であることを示す（DI対象になる）
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    @Autowired // コンストラクタによる依存性注入
    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    /**
     * 指定された evaluatorId に対応する提出期間中の評価依頼一覧を取得する。
     * 
     * 評価済み・未評価問わず、フェーズの期間内であれば取得対象とする。
     */
    public List<EvaluationResponseDTO> getEvaluationsInPeriod(Long evaluatorId) {
        return evaluationRepository.findEvaluationsInPeriod(evaluatorId);
    }
}
