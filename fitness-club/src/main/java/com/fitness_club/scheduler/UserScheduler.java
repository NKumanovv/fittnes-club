package com.fitness_club.scheduler;

import com.fitness_club.user.model.User;
import com.fitness_club.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class UserScheduler {

    private final UserService userService;

    @Autowired
    public UserScheduler(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 9 * * MON")
    public void sendWeeklyMotivation() {
        log.info("[CRON JOB] Starting Weekly Motivation Email Blast at %s".formatted(LocalDateTime.now()));

        List<User> users = userService.getAllUsers();
        int emailCount = 0;

        for (User user : users) {
            if (user.isActive()) {
                log.info("   -> Sending email to: [%s]".formatted(user.getUsername()));
                emailCount++;
            }
        }

        log.info("[CRON JOB] Completed. Emails sent: %s".formatted(emailCount));
    }
}