package com.example.api.service;

import com.example.api.dto.PhaseResponse;
import com.example.api.entity.Phase;
import com.example.api.repository.PhaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhaseService {

    private final PhaseRepository phaseRepository;

    public PhaseService(PhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    public List<PhaseResponse> getPhases() {
        List<Phase> phases = phaseRepository.findAll();

        return phases.stream().map(phase -> {
            PhaseResponse dto = new PhaseResponse();
            dto.setPhaseId(phase.getId());

            dto.setPhaseNumber(phase. getPhaseNumber());
            dto.setName(phase.getName());
            
            dto.setStartDate(phase.getStartDate());
            dto.setEndDate(phase.getEndDate());

            // isClosed判定(end_dateが現在日付よりも前ならtrue)
            boolean isClosed = false;
            if (phase.getEndDate() != null) {
                isClosed = phase.getEndDate().isBefore(LocalDate.now());
            }
            dto.setClosed(isClosed);
            
            return dto;
        }).collect(Collectors.toList());
    }
}