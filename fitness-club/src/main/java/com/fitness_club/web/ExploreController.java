package com.fitness_club.web;

import com.fitness_club.meal.service.MealService;
import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.service.UserService;
import com.fitness_club.workout.repository.WorkoutRepository;
import com.fitness_club.workout.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/explore")
@RequiredArgsConstructor
public class ExploreController {

    private final WorkoutService workoutService;
    private final MealService mealService;
    private final UserService userService;

    @GetMapping
    public ModelAndView getExplorePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("explore");

        modelAndView.addObject("user", userService.getById(authenticationMetadata.getUserId()));
        modelAndView.addObject("publicWorkouts", workoutService.getAllPublicWorkouts());
        modelAndView.addObject("publicMeals", mealService.getAllPublicMeals());

        return modelAndView;
    }
}