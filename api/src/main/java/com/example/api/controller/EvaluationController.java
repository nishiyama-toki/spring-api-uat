
package com.example.api.controller; // コントローラーのパッケージ定義

import com.example.api.dto.EvaluationResponseDTO; // レスポンス用DTO
import com.example.api.service.EvaluationService; // 評価取得サービス
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // このクラスはREST APIのコントローラーであることを示す
@RequestMapping("/api/evaluations") // このクラスのAPIパスのプレフィックスを定義
public class EvaluationController {

    private final EvaluationService evaluationService;

    @Autowired // コンストラクタインジェクション
    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    /**
     * evaluatorId を指定して、そのユーザーに対する提出期間中の評価依頼を取得する。
     *
     * @param evaluatorId 評価者ID（クエリパラメータで受け取る）
     * @return フェーズ内の評価依頼一覧（提出済み・未済問わず）
     */
    @GetMapping
    public List<EvaluationResponseDTO> getEvaluations(@RequestParam Long evaluatorId) {
        return evaluationService.getEvaluationsInPeriod(evaluatorId); // 新メソッドに変更
    }
}
