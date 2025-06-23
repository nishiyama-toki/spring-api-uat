package com.example.api.controller;
<<<<<<< HEAD

import com.example.api.dto.EvaluationResponseDTO;
import com.example.api.service.EvaluationService;
import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
=======

import com.example.api.dto.EvaluationResponseDTO;
import com.example.api.entity.Employee;
import com.example.api.entity.Evaluation;
import com.example.api.repository.EmployeeRepository;
import com.example.api.service.EvaluationService;

>>>>>>> 6634e23 (コンパイルエラー再度解消)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
<<<<<<< HEAD
public class EvaluationController {
=======
public class EvaluationsController {
>>>>>>> 6634e23 (コンパイルエラー再度解消)

    private final EvaluationService   evaluationService;
    private final EmployeeRepository  employeeRepository;

    @Autowired
<<<<<<< HEAD
    public EvaluationController(EvaluationService evaluationService,
                               EmployeeRepository employeeRepository) {
        this.evaluationService = evaluationService;
        this.employeeRepository = employeeRepository;
    }

    /**
     * ログイン中のユーザーに対して、提出期間中の評価依頼を取得する。
     *
     * @param loginUser ログイン中のユーザー情報（JWTから自動で取得）
     * @return フェーズ内の評価依頼一覧（提出済み・未済問わず）
     */
    @GetMapping
    public List<EvaluationResponseDTO> getEvaluations(@AuthenticationPrincipal UserDetails loginUser) {
        String email = loginUser.getUsername();
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("社員が見つかりません"));
        Long evaluatorId = employee.getId();
        return evaluationService.getEvaluationsInPeriod(evaluatorId);
=======
    public EvaluationsController(EvaluationService evaluationService,
                                 EmployeeRepository employeeRepository) {
        this.evaluationService  = evaluationService;
        this.employeeRepository = employeeRepository;
    }

    /** ログイン中ユーザー向け：提出対象の評価依頼一覧を返す */
    @GetMapping
    public List<EvaluationResponseDTO> getEvaluations(
            @AuthenticationPrincipal UserDetails loginUser) {

        // ① ログインユーザー（= evaluator）を取得
        String   email     = loginUser.getUsername();
        Employee evaluator = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("社員が見つかりません"));
        Long evaluatorId   = evaluator.getId();

        // ② Entity のリストを Service から取得
        List<Evaluation> entities =
                evaluationService.getEvaluationsInPeriod(evaluatorId);

        // ③ DTO に変換してフロントへ返却
        return entities.stream()
                       .map(EvaluationResponseDTO::fromEntity)
                       .toList();
>>>>>>> 6634e23 (コンパイルエラー再度解消)
    }
}
