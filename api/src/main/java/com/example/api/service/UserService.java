package com.example.api.service;

import com.example.api.dto.UserRegisterDto;
import com.example.api.dto.TargetResponseDto;
import com.example.api.dto.GetEvaluationDto; // ← リネーム済み！
import com.example.api.entity.User;
import com.example.api.entity.Evaluation;
import com.example.api.repository.UserRepository;
import com.example.api.repository.EvaluationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegisterDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        // パスワードをハッシュ化してセット
        String hashed = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hashed);

        user.setAdmin(dto.isAdmin());
        return userRepository.save(user);
    }

    // ----------------------------
    // 多面評価対象者＋評価済みデータを取得
    // ----------------------------
    public List<TargetResponseDto> getTargetsWithEvaluation(Integer evaluatorId) {
        List<User> targets = userRepository.findAll(); // 実際は対象者の精精り込みも可能
        List<TargetResponseDto> result = new ArrayList<>();

        for (User target : targets) {
            if (target.getId().equals(evaluatorId)) continue; // 自分を除外したい場合

            TargetResponseDto dto = new TargetResponseDto();
            dto.setId(target.getId());
            dto.setName(target.getName());
            dto.setRole(target.getRole());

            evaluationRepository
                .findByEvaluatorIdAndTargetId(evaluatorId, target.getId())
                .ifPresent(evaluation -> {
                    GetEvaluationDto evalDto = new GetEvaluationDto();
                    evalDto.setSkill_score(evaluation.getSkill_score());
                    evalDto.setBusiness_score(evaluation.getBusiness_score());
                    evalDto.setTeam_score(evaluation.getTeam_score());
                    evalDto.setComment(evaluation.getComment());

                    dto.setEvaluation(evalDto); // ← ここでセット！
                });

            result.add(dto);
        }

        return result;
    }
}
