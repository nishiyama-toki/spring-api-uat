package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    // 1分ごとにメール送信をテスト用に実行
    @Scheduled(cron = "0 */1 * * * ?")
    public void sendReminderEmails() {
        Optional<Phase> currentPhaseOpt = phaseRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now());

        if (currentPhaseOpt.isPresent()) {
            Phase currentPhase = currentPhaseOpt.get();
            
            List<Employee> employeesNotSubmitted = evaluationRepository.findEmployeesNotSubmitted(currentPhase.getId());

            for (Employee employee : employeesNotSubmitted) {
                mailService.sendReminder(
                    employee.getEmail(),
                    employee.getName(),
                    currentPhase.getName(),
                    currentPhase.getSelfEvalDue().toString()
                );
            }
        } else {
            System.out.println("現在有効な評価フェーズはありません。");
        }
    }
}
