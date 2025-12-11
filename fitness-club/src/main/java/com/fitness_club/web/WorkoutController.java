package com.fitness_club.web;

import com.fitness_club.client.HistoryClient;
import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.service.UserService;
import com.fitness_club.web.dto.CreateWorkoutRequest;
import com.fitness_club.web.dto.WorkoutLogRequest;
import com.fitness_club.web.mapper.DtoMapper;
import com.fitness_club.workout.model.Workout;
import com.fitness_club.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/workouts")
public class WorkoutController {

    private final UserService userService;
    private final WorkoutService workoutService;
    private final HistoryClient historyClient;

    @Autowired
    public WorkoutController(UserService userService, WorkoutService workoutService, HistoryClient historyClient) {
        this.userService = userService;
        this.workoutService = workoutService;
        this.historyClient = historyClient;
    }

    @GetMapping
    public ModelAndView getWorkoutsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());

        List<Workout> workouts = workoutService.getAllWorkoutsById(user.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("workouts");
        modelAndView.addObject("user", user);
        modelAndView.addObject("workouts", workouts);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getWorkoutPage(@PathVariable UUID id) {
        Workout workout = workoutService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("workout");
        modelAndView.addObject("workout", workout);
        modelAndView.addObject("createWorkoutRequest", DtoMapper.mapWorkoutToCreateWorkoutRequest(workout));

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView getNewWorkoutPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new-workout");
        modelAndView.addObject("createWorkoutRequest", new CreateWorkoutRequest());

        return modelAndView;
    }

    @PutMapping("/{id}")
    public ModelAndView updateWorkout(@PathVariable UUID id, @Valid CreateWorkoutRequest createWorkoutRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Workout workout = workoutService.getById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("workout");
            modelAndView.addObject("workout", workout);
            modelAndView.addObject("createWorkoutRequest", createWorkoutRequest);
            return modelAndView;
        }

        workoutService.editWorkoutDetails(id, createWorkoutRequest);

        return new ModelAndView("redirect:/workouts");
    }

    @PostMapping("/new")
    public ModelAndView createWorkout(@Valid CreateWorkoutRequest createWorkoutRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("new-workout");
            modelAndView.addObject("createWorkoutRequest", createWorkoutRequest);
            return modelAndView;
        }

        User user = userService.getById(authenticationMetadata.getUserId());
        workoutService.createWorkout(user, createWorkoutRequest);

        return new ModelAndView("redirect:/workouts");
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteWorkout(@PathVariable UUID id) {
        workoutService.deleteWorkout(id);
        return new ModelAndView("redirect:/workouts");
    }

    @PostMapping("/{id}/complete")
    public ModelAndView completeWorkout(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata metadata) {

        Workout workout = workoutService.getById(id);

        WorkoutLogRequest workoutLogRequest = WorkoutLogRequest.builder()
                .userId(metadata.getUserId())
                .workoutName(workout.getName())
                .duration(workout.getDuration())
                .build();

        historyClient.logWorkout(workoutLogRequest);
        log.info("User [%s] completed workout ID [%s] - sending to History Service.".formatted(metadata.getUsername(), id));

        return new ModelAndView("redirect:/workouts");
    }
}