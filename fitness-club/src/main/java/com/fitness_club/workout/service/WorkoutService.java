package com.fitness_club.workout.service;

import com.fitness_club.user.model.User;
import com.fitness_club.web.dto.CreateWorkoutRequest;
import com.fitness_club.workout.model.Workout;
import com.fitness_club.workout.repository.WorkoutRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Workout getById(UUID id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout with id " + id + " not found"));
    }

    public void createWorkout(User user, CreateWorkoutRequest request) {
        Workout workout = new Workout();
        workout.setName(request.getName());
        workout.setDescription(request.getDescription());
        workout.setDifficulty(request.getDifficulty());
        workout.setDuration(request.getDuration());
        workout.setUser(user);

        workoutRepository.save(workout);
    }

    public void editWorkoutDetails(UUID id, CreateWorkoutRequest request) {
        Workout workout = getById(id);

        workout.setName(request.getName());
        workout.setDescription(request.getDescription());
        workout.setDifficulty(request.getDifficulty());
        workout.setDuration(request.getDuration());

        workoutRepository.save(workout);
    }
}