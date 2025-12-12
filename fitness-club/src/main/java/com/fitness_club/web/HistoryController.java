package com.fitness_club.web;

import com.fitness_club.client.HistoryClient;
import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.service.UserService;
import com.fitness_club.web.dto.WorkoutLogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private final HistoryClient historyClient;
    private final UserService userService;

    @Autowired
    public HistoryController(HistoryClient historyClient, UserService userService) {
        this.historyClient = historyClient;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getHistoryPage(@AuthenticationPrincipal AuthenticationMetadata metadata) {

        List<WorkoutLogResponse> logs = historyClient.getUserHistory(metadata.getUserId());

        ModelAndView modelAndView = new ModelAndView("history");
        modelAndView.addObject("logs", logs);
        modelAndView.addObject("user", userService.getById(metadata.getUserId()));

        return modelAndView;
    }
}