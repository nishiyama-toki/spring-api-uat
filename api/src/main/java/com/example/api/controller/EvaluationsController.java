package com.example.api.controller; // コントローラーのパッケージ定義

import com.example.api.dto.EvaluationResponseDTO; // レスポンス用DTO
import com.example.api.service.EvaluationService; // 評価取得サービス
import com.example.api.entity.Employee; // DBから評価者を取得するためのエンティティ
import com.example.api.repository.EmployeeRepository; // DBから評価者を取得するためのリポジトリ
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 認証済みユーザーを受け取るアノテーション
import org.springframework.security.core.userdetails.UserDetails; // UserDetailsの型で受け取る
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // このクラスはREST APIのコントローラーであることを示す
@RequestMapping("/api/evaluations") // このクラスのAPIパスのプレフィックスを定義
public class EvaluationControllers {

    private final EvaluationService evaluationService;
    private final EmployeeRepository employeeRepository;

    @Autowired // コンストラクタインジェクション
    public EvaluationController(EvaluationService evaluationService,
                                EmployeeRepository employeeRepository) {
        this.evaluationService = evaluationService;
        this.employeeRepository = employeeRepository;
    }

    /**
     * ログイン中のユーザーに対して、提出期間中の評価依頼を取得する。
     * evaluatorId は JWT トークンから取得された UserDetails からemailを取得し、DBから評価者IDを取得する。
     *
     * @param loginUser ログイン中のユーザー情報（JWTから自動で取得）
     * @return フェーズ内の評価依頼一覧（提出済み・未済問わず）
     */
    @GetMapping
    public List<EvaluationResponseDTO> getEvaluations(@AuthenticationPrincipal UserDetails loginUser) {
        String email = loginUser.getUsername(); // ← 通常、username = email として扱われている
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("社員が見つかりません"));
        Long evaluatorId = employee.getId(); // ← DBから取得した社員ID
        return evaluationService.getEvaluationsInPeriod(evaluatorId); // 評価取得サービスを呼び出し
    }
}