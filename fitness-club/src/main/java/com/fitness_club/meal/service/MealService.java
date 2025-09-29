package com.fitness_club.meal.service;

import com.fitness_club.exeption.DomainException;
import com.fitness_club.meal.model.Meal;
import com.fitness_club.meal.repository.MealRepository;
import com.fitness_club.user.model.User;
import com.fitness_club.web.dto.CreateMealRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }


    public Meal initializeMeal(CreateMealRequest createMealRequest){
        return Meal.builder()
                .name(createMealRequest.getName())
                .calories(createMealRequest.getCalories())
                .protein(createMealRequest.getProtein())
                .carbs(createMealRequest.getCarbs())
                .fats(createMealRequest.getFats())
                .isPublic(createMealRequest.isPublic())
                .build();
    }

    public Meal getById(UUID id){
        return mealRepository.findMealById(id).orElseThrow(() -> new DomainException("Meal with id [%s] does not exist.".formatted(id)));
    }


    public void editMealDetails(UUID mealId, CreateMealRequest createMealRequest) {

        Meal meal = getById(mealId);

        //if (userEditRequest.getEmail().isBlank()) {
        //    notificationService.saveNotificationPreference(userId, false, null);
        //}

        meal.setName(createMealRequest.getName());
        meal.setCalories(createMealRequest.getCalories());
        meal.setProtein(createMealRequest.getProtein());
        meal.setCarbs(createMealRequest.getCarbs());
        meal.setFats(createMealRequest.getFats());
        meal.setPublic(createMealRequest.isPublic());

        //if (!userEditRequest.getEmail().isBlank()) {
        //    notificationService.saveNotificationPreference(userId, true, userEditRequest.getEmail());
        //}

        mealRepository.save(meal);
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
}
