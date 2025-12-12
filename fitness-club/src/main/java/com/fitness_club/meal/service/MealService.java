package com.fitness_club.meal.service;

import com.fitness_club.exeption.DomainException;
import com.fitness_club.meal.model.Meal;
import com.fitness_club.meal.repository.MealRepository;
import com.fitness_club.user.model.User;
import com.fitness_club.web.dto.CreateMealRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }


    public Meal initializeMeal(User user, CreateMealRequest createMealRequest){
        return Meal.builder()
                .name(createMealRequest.getName())
                .calories(createMealRequest.getCalories())
                .protein(createMealRequest.getProtein())
                .carbs(createMealRequest.getCarbs())
                .fats(createMealRequest.getFats())
                .isPublic(createMealRequest.isPublic())
                .user(user)
                .build();
    }

    @CacheEvict(value = "meals",  allEntries = true)
    public Meal createMeal(User user, CreateMealRequest createMealRequest){
        log.info("Creating new meal [%s] for user [%s]".formatted(createMealRequest.getName(), user.getUsername()));
        return mealRepository.save(initializeMeal(user, createMealRequest));
    }

    @Cacheable(value = "meals")
    public Meal getById(UUID id){
        return mealRepository.findMealById(id).orElseThrow(() -> new DomainException("Meal with id [%s] does not exist.".formatted(id)));
    }


    @CacheEvict(value = "meals", allEntries = true)
    public void editMealDetails(UUID mealId, CreateMealRequest createMealRequest) {

        Meal meal = getById(mealId);

        meal.setName(createMealRequest.getName());
        meal.setCalories(createMealRequest.getCalories());
        meal.setProtein(createMealRequest.getProtein());
        meal.setCarbs(createMealRequest.getCarbs());
        meal.setFats(createMealRequest.getFats());
        meal.setPublic(createMealRequest.isPublic());

        mealRepository.save(meal);
        log.info("Editing meal ID [%s].".formatted( mealId));
    }

    public void createFirstMeal(User user){
        Meal meal = mealRepository.save(initializeFirstMeal(user));
    }

    public Meal initializeFirstMeal(User user) {
        return Meal.builder()
                .name("Chicken and rice")
                .calories(600.0)
                .protein(50.0)
                .carbs(50.0)
                .fats(5.0)
                .isPublic(false)
                .user(user)
                .build();
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void deleteMeal(UUID id) {
        mealRepository.deleteById(id);
        log.warn("Deleting meal ID %s}]".formatted(id));
    }

    @Cacheable(value = "meals")
    public List<Meal> getAllMealsByUserId(UUID id) {
        return mealRepository.getAllMealsByUser_Id(id);
    }

    @Cacheable(value = "public_meals")
    public List<Meal> getAllPublicMeals() {
        log.info("Fetching all PUBLIC meals from Database...");
        return mealRepository.findByIsPublicTrue();
    }
}
