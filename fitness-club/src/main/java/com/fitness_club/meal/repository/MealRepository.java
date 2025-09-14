package com.fitness_club.meal.repository;

import com.fitness_club.meal.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID> {

    Optional<Meal> findMealById(UUID id);

}
