package com.example.api.service;

import com.example.api.dto.UnsubmittedResponse;
import com.example.api.entity.Employee;
import com.example.api.repository.EvaluationRepository;
import com.example.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnsubmittedService {

    private final EvaluationRepository evaluationRepository;
    private final EmployeeRepository employeeRepository;

    public List<UnsubmittedResponse> findUnsubmittedByPhase(Long phaseId) {
        List<Long> submittedIds = evaluationRepository.findEvaluatorIdsByPhaseId(phaseId);

        List<Employee> allEmployees = employeeRepository.findAll();

        return allEmployees.stream()
            .filter(e ->  !submittedIds.contains(e.getId()))
            .map(e -> new UnsubmittedResponse(e.getName()))
            .collect(Collectors.toList());
    }
}