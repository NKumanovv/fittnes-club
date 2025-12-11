package com.fitness_club.meal.repository;

import com.fitness_club.meal.model.Meal;
import com.fitness_club.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID> {

    Optional<Meal> findMealById(UUID id);

    List<Meal> getAllMealsByUser_Id(UUID id);


}
