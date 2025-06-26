package com.example.api.service;

import com.example.api.dto.UserRegisterDto;
import com.example.api.dto.TargetResponseDto;
import com.example.api.dto.GetEvaluationDto;

import com.example.api.entity.Employee;
import com.example.api.entity.Evaluation;

import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.EvaluationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                });

            result.add(dto);
        }

        return result;
    }
}
