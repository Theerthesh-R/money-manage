package com.theerthesh.moneyManager.service;


import com.theerthesh.moneyManager.dto.ExpenseDto;
import com.theerthesh.moneyManager.entity.ProfileEntity;
import com.theerthesh.moneyManager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {


    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    // @Scheduled(cron = "0 * * * * *",zone = "IST")
    @Scheduled(cron = "0 0 22 * * *", zone = "IST")
    public void sendDailyIncomeExpenseReminder() {
        log.info("Job Started send");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles) {
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in Money Manager.<br><br>"
                    + "<a href=\"" + frontendUrl + "\" style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Money Manager</a>"
                    + "<br><br>Best regards,<br>Money Manager Team";

            emailService.sendMail(profile.getEmail(), "Daily Reminder :Add you income and Expenses", body);
        }
        log.info("Job complete send");

    }

    @Scheduled(cron = "0 0 23 * * *")
// @Scheduled(cron = "0 0 23 * * *") // Runs daily at 11 PM
    public void sendDailyExpenseSummary() {
        log.info("Job Started: send expense summary");


        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {
            List<ExpenseDto> todaysExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());

            if (!todaysExpenses.isEmpty()) {
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>")
                        .append("<tr style='background-color:#f2f2f2;'>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>#</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Name</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Date</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Category</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Amount</th>")
                        .append("</tr>");

                int i = 1;
                for (ExpenseDto expenseDto : todaysExpenses) {
                    table.append("<tr>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(profile.getFullName()).append("</td>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(expenseDto.getDate()).append("</td>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>")
                            .append(expenseDto.getCategoryId() != null ? expenseDto.getCategoryName() : "N/A").append("</td>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(expenseDto.getAmount()).append("</td>")
                            .append("</tr>");
                }

                table.append("</table>");

                String body = "Hi " + profile.getFullName() + ",<br/><br/>"
                        + "Here is a summary of your expenses for today:<br/><br/>"
                        + table.toString()
                        + "<br/><br/>Thank you.";

                emailService.sendMail(profile.getEmail(), "Your Daily Expense Summary", body);
            }
        }

        log.info("Job completed: send daily summary");
    }
}
