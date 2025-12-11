package com.fitness_club.workout.service;

import com.fitness_club.exeption.DomainException;
import com.fitness_club.workout.model.Difficulty;
import com.fitness_club.workout.model.Workout;
import com.fitness_club.workout.repository.WorkoutRepository;
import com.fitness_club.user.model.User;
import com.fitness_club.web.dto.CreateWorkoutRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Workout initializeWorkout(User user, CreateWorkoutRequest createWorkoutRequest) {
        return Workout.builder()
                .name(createWorkoutRequest.getName())
                .description(createWorkoutRequest.getDescription())
                .difficulty(createWorkoutRequest.getDifficulty())
                .duration(createWorkoutRequest.getDuration())
                .user(user)
                .build();
    }

    @CacheEvict(value = "workouts", allEntries = true)
    public Workout createWorkout(User user, CreateWorkoutRequest createWorkoutRequest) {
        log.info("Creating new workout [%s] for user [%s]".formatted(createWorkoutRequest.getName(), user.getUsername()));
        return workoutRepository.save(initializeWorkout(user, createWorkoutRequest));
    }

    @Cacheable(value = "workouts")
    public Workout getById(UUID id) {
        return workoutRepository.findById(id).orElseThrow(() -> new DomainException("Workout with id [%s] does not exist.".formatted(id)));
    }

    @CacheEvict(value = "workouts", allEntries = true)
    public void editWorkoutDetails(UUID workoutId, CreateWorkoutRequest createWorkoutRequest) {

        Workout workout = getById(workoutId);

        workout.setName(createWorkoutRequest.getName());
        workout.setDescription(createWorkoutRequest.getDescription());
        workout.setDifficulty(createWorkoutRequest.getDifficulty());
        workout.setDuration(createWorkoutRequest.getDuration());

        workoutRepository.save(workout);
        log.info("Editing workout ID [%s]. New Name: [%s]".formatted( workoutId, createWorkoutRequest.getName()));
    }

    public void createFirstWorkout(User user) {
        workoutRepository.save(initializeFirstWorkout(user));
    }

    public Workout initializeFirstWorkout(User user) {
        return Workout.builder()
                .name("Bicep workout")
                .description("Arms like banitsa")
                .difficulty(Difficulty.ADVANCED)
                .duration(60)
                .user(user)
                .build();
    }

    @CacheEvict(value = "workouts", allEntries = true)
    public void deleteWorkout(UUID id) {
        workoutRepository.deleteById(id);
        log.info("Deleting history log with ID: [%s]".formatted(id));
    }

    @Cacheable(value = "workouts")
    public List<Workout> getAllWorkoutsById(UUID id) {
        return workoutRepository.getAllWorkoutsByUser_Id(id);
    }
}