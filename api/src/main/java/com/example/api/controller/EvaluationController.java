
package com.example.api.controller; // コントローラーのパッケージ定義

import com.example.api.dto.EvaluationResponseDTO; // レスポンス用DTO
import com.example.api.service.EvaluationService; // 評価取得サービス
import com.example.api.security.LoginUserDetails; // JWTから取り出すログインユーザー情報（UserDetails実装）
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 認証済みユーザーを受け取るアノテーション
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
     * ログイン中のユーザーに対して、提出期間中の評価依頼を取得する。
     * evaluatorId は JWT トークンから取得された LoginUserDetails から取得する。
     *
     * @param loginUser ログイン中のユーザー情報（JWTから自動で取得）
     * @return フェーズ内の評価依頼一覧（提出済み・未済問わず）
     */
    @GetMapping
    public List<EvaluationResponseDTO> getEvaluations(@AuthenticationPrincipal LoginUserDetails loginUser) {
        Long evaluatorId = loginUser.getUserId(); // JWTトークン内のユーザーIDを取得
        return evaluationService.getEvaluationsInPeriod(evaluatorId); // 評価取得サービスを呼び出し
    }
}
