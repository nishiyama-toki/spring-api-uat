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

    @Scheduled(cron = "0 */1 * * * ?") // 1分ごとに実行
    public void sendReminderEmails() {
        List<Phase> phases = phaseRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now());

        if (phases.isEmpty()) {
            System.out.println("現在有効な評価フェーズはありません。");
            return;
        }

        Phase currentPhase = phases.get(0);

        // currentPhase.getSelfEvalDue() の null チェックと警告メッセージを削除
        // if (currentPhase.getSelfEvalDue() == null) {
        //     System.out.println("警告: 該当フェーズの selfEvalDue が null です（フェーズ名: " + currentPhase.getName() + "）");
        //     return;
        // }

        List<Employee> employeesNotSubmitted = evaluationRepository.findEmployeesNotSubmitted(currentPhase.getId());

        // リマインダーの期限文字列を生成
        // selfEvalDue が削除されたため、代わりに endDate を使用するか、別の期限情報を利用
        String dueDateString = (currentPhase.getEndDate() != null) ? currentPhase.getEndDate().toString() : "期限不明";

        for (Employee employee : employeesNotSubmitted) {
            mailService.sendReminder(
                employee.getEmail(),
                employee.getName(),
                currentPhase.getName(),
                dueDateString // 修正後の期限文字列を使用
            );
        }
        System.out.println("リマインダーメールの送信処理が完了しました。"); // 処理完了メッセージを追加
    }
}