package com.fitness_club.web;

import com.fitness_club.security.AuthenticationMetadata;
import com.fitness_club.user.model.User;
import com.fitness_club.user.service.UserService;
import com.fitness_club.web.dto.CreateWorkoutRequest;
import com.fitness_club.web.mapper.DtoMapper;
import com.fitness_club.workout.model.Workout;
import com.fitness_club.workout.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final UserService userService;
    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(UserService userService, WorkoutService workoutService) {
        this.userService = userService;
        this.workoutService = workoutService;
    }

    @GetMapping
    public ModelAndView getWorkoutsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());
        // Assuming your User entity has a List<Workout> workouts field
        List<Workout> workouts = user.getWorkouts();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("workouts"); // corresponds to workouts.html
        modelAndView.addObject("user", user);
        modelAndView.addObject("workouts", workouts);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getWorkoutPage(@PathVariable UUID id) {
        Workout workout = workoutService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("workout"); // corresponds to workout.html (details/edit view)
        modelAndView.addObject("workout", workout);
        modelAndView.addObject("createWorkoutRequest", DtoMapper.mapWorkoutToCreateWorkoutRequest(workout));

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView getNewWorkoutPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new-workout"); // corresponds to new-workout.html
        modelAndView.addObject("createWorkoutRequest", new CreateWorkoutRequest());

        return modelAndView;
    }

    @PutMapping("/{id}")
    public ModelAndView updateWorkout(@PathVariable UUID id,
                                      @Valid CreateWorkoutRequest createWorkoutRequest,
                                      BindingResult bindingResult) {

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
    public ModelAndView createWorkout(@Valid CreateWorkoutRequest createWorkoutRequest,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

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
}