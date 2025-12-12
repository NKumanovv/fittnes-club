package com.fitness_club.workout.repository;

import com.fitness_club.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> getAllWorkoutsByUser_Id(UUID id);

    List<Workout> findByIsPublicTrue();
}
