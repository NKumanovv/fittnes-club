package com.fitness_club.CacheScheduler;

import com.fitness_club.meal.service.MealService;
import com.fitness_club.workout.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheScheduler {

    private final WorkoutService workoutService;
    private final MealService mealService;

    @Autowired
    public CacheScheduler(WorkoutService workoutService, MealService mealService) {
        this.workoutService = workoutService;
        this.mealService = mealService;
    }


    @Scheduled(fixedRate = 1000)
    @CacheEvict(value = {"public_workouts", "public_meals"}, allEntries = true)
    public void refreshPublicCaches() {
        log.info("[Scheduler] Warming up public caches...");

        workoutService.getAllPublicWorkouts();
        mealService.getAllPublicMeals();

        log.info("[Scheduler] Public workouts and meals refreshed in Redis.");
    }
}