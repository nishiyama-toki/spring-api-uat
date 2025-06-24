package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import com.example.api.entity.Employee;
import com.example.api.entity.Phase;
import com.example.api.repository.EvaluationRepository;
import com.example.api.repository.PhaseRepository;

@Component
public class ScheduledTasks {

    @Autowired
    private MailService mailService;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Scheduled(cron = "0 */1 * * * ?")
    public void sendReminderEmails() {
        List<Phase> phases = phaseRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now());

        if (phases.isEmpty()) {
            System.out.println("現在有効な評価フェーズはありません。");
            return;
        }

        Phase currentPhase = phases.get(0);

        List<Employee> employeesNotSubmitted = evaluationRepository.findEmployeesNotSubmitted(currentPhase.getId());

        for (Employee employee : employeesNotSubmitted) {
            mailService.sendReminder(
                employee.getEmail(),
                employee.getName(),
                currentPhase.getName(),
                currentPhase.getSelfEvalDue().toString()
            );
        }
    }
}
