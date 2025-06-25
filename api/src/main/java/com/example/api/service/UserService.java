package com.example.api.service;

import com.example.api.dto.UserRegisterDto;
import com.example.api.dto.TargetResponseDto;
<<<<<<< HEAD
import com.example.api.dto.GetEvaluationDto; // ← リネーム済み！
import com.example.api.entity.User;
import com.example.api.entity.Evaluation;
import com.example.api.repository.UserRepository;
=======
import com.example.api.dto.GetEvaluationDto;

import com.example.api.entity.Employee;
import com.example.api.entity.Evaluation;

import com.example.api.repository.EmployeeRepository;
>>>>>>> mizukami
import com.example.api.repository.EvaluationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.Optional;
>>>>>>> mizukami

@Service
public class UserService {

    @Autowired
<<<<<<< HEAD
    private UserRepository userRepository;
=======
    private EmployeeRepository employeeRepository;
>>>>>>> mizukami

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

<<<<<<< HEAD
    public User registerUser(UserRegisterDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        // パスワードをハッシュ化してセット
        String hashed = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hashed);

        user.setAdmin(dto.isAdmin());
        user.setPermission(dto.getPermission());
        return userRepository.save(user);
    }

    // ----------------------------
    // 多面評価対象者＋評価済みデータを取得
    // ----------------------------
    public List<TargetResponseDto> getTargetsWithEvaluation(Long evaluatorId) {
        List<User> targets = userRepository.findAll(); // 実際は対象者の精精り込みも可能
        List<TargetResponseDto> result = new ArrayList<>();

        for (User target : targets) {
            if (target.getId().equals(evaluatorId)) continue; // 自分を除外したい場合

            TargetResponseDto dto = new TargetResponseDto();
            dto.setId(target.getId());
            dto.setName(target.getName());
            dto.setRole(target.getRole());

            evaluationRepository
                .findByEvaluatorIdAndTargetId(evaluatorId, target.getId().longValue())
                .ifPresent(evaluation -> {
                    GetEvaluationDto evalDto = new GetEvaluationDto();
                    evalDto.setSkill_score(evaluation.getSkillScore().floatValue());
                    evalDto.setBusiness_score(evaluation.getBusinessScore().floatValue());
                    evalDto.setTeam_score(evaluation.getTeamScore().floatValue());

                    evalDto.setComment(evaluation.getComment());

                    dto.setEvaluation(evalDto); // ← ここでセット！
=======
    public Employee registerUser(UserRegisterDto dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());

        String hashed = passwordEncoder.encode(dto.getPassword());
        employee.setPassword(hashed);

        employee.setIsAdmin(dto.isAdmin());
        // 権限はpermissionで管理されているため、setRoleではなくsetPermissionを使用
        employee.setRole(dto.getRole() != null ? dto.getRole() : "USER"); // もしdtoにroleがあるならそのまま、なければデフォルト値
        employee.setPermission(dto.getPermission() != null ? dto.getPermission() : "USER"); // permissionで権限を設定

        return employeeRepository.save(employee);
    }

    public List<TargetResponseDto> getTargetsWithEvaluation(Long evaluatorId, Long phaseId) {
        List<Employee> allEmployees = employeeRepository.findAll();
        List<TargetResponseDto> result = new ArrayList<>();

        for (Employee targetEmployee : allEmployees) {
            // 自分自身は評価対象から除外
            if (targetEmployee.getId().equals(evaluatorId)) {
                continue;
            }

            TargetResponseDto dto = new TargetResponseDto();
            dto.setId(targetEmployee.getId());
            dto.setName(targetEmployee.getName());
            // 権限はpermissionで管理されているため、getRoleではなくgetPermissionを使用
            dto.setRole(targetEmployee.getPermission() != null ? targetEmployee.getPermission() : ""); // <-- ここを修正

            Optional<Evaluation> existingEvaluation = evaluationRepository
                .findByEvaluatorIdAndTargetIdAndPhaseId(evaluatorId, targetEmployee.getId(), phaseId);

            existingEvaluation
                .ifPresent(evaluation -> {
                    GetEvaluationDto evalDto = new GetEvaluationDto();
                    evalDto.setSkill_score(evaluation.getSkillScore() != null ? evaluation.getSkillScore().floatValue() : null);
                    evalDto.setBusiness_score(evaluation.getBusinessScore() != null ? evaluation.getBusinessScore().floatValue() : null);
                    evalDto.setTeam_score(evaluation.getTeamScore() != null ? evaluation.getTeamScore().floatValue() : null);
                    evalDto.setComment(evaluation.getComment());
                    dto.setEvaluation(evalDto);
>>>>>>> mizukami
                });

            result.add(dto);
        }

        return result;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> mizukami
