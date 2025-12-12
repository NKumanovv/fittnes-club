package com.fitness_club.meal.service;

import com.fitness_club.exeption.DomainException;
import com.fitness_club.meal.model.Meal;
import com.fitness_club.meal.repository.MealRepository;
import com.fitness_club.user.model.User;
import com.fitness_club.web.dto.CreateMealRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealService mealService;


    @Test
    void createMeal_ValidRequest_SavesMeal() {
        User user = User.builder().username("testuser").build();
        CreateMealRequest request = new CreateMealRequest();
        request.setName("Steak");
        request.setCalories(800.0);
        request.setProtein(60.0);
        request.setCarbs(10.0);
        request.setFats(40.0);
        request.setPublic(true);

        when(mealRepository.save(any(Meal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Meal result = mealService.createMeal(user, request);

        assertNotNull(result);
        assertEquals("Steak", result.getName());
        assertEquals(800.0, result.getCalories());
        assertTrue(result.isPublic());
        assertEquals(user, result.getUser());
        verify(mealRepository).save(any(Meal.class));
    }


    @Test
    void getById_MealExists_ReturnsMeal() {
        UUID id = UUID.randomUUID();
        Meal meal = new Meal();
        meal.setId(id);

        when(mealRepository.findMealById(id)).thenReturn(Optional.of(meal));

        Meal result = mealService.getById(id);

        assertEquals(meal, result);
    }

    @Test
    void getById_NotFound_ThrowsDomainException() {
        UUID id = UUID.randomUUID();
        when(mealRepository.findMealById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> mealService.getById(id));
    }


    @Test
    void editMealDetails_ExistingMeal_UpdatesFieldsAndSaves() {
        UUID id = UUID.randomUUID();
        Meal existingMeal = new Meal();
        existingMeal.setId(id);
        existingMeal.setName("Old Name");

        CreateMealRequest updateRequest = new CreateMealRequest();
        updateRequest.setName("New Name");
        updateRequest.setCalories(500.0);
        updateRequest.setProtein(30.0);
        updateRequest.setCarbs(20.0);
        updateRequest.setFats(10.0);
        updateRequest.setPublic(false);

        when(mealRepository.findMealById(id)).thenReturn(Optional.of(existingMeal));

        mealService.editMealDetails(id, updateRequest);

        assertEquals("New Name", existingMeal.getName());
        assertEquals(500.0, existingMeal.getCalories());
        assertFalse(existingMeal.isPublic());
        verify(mealRepository).save(existingMeal);
    }


    @Test
    void deleteMeal_ValidId_CallsRepository() {
        UUID id = UUID.randomUUID();

        mealService.deleteMeal(id);

        verify(mealRepository).deleteById(id);
    }


    @Test
    void createFirstMeal_SavesDefaultMeal() {
        User user = new User();

        mealService.createFirstMeal(user);

        verify(mealRepository).save(argThat(meal ->
                meal.getName().equals("Chicken and rice") &&
                        meal.getCalories() == 600.0 &&
                        meal.getUser() == user
        ));
    }


    @Test
    void getAllMealsByUserId_ReturnsList() {
        UUID userId = UUID.randomUUID();
        when(mealRepository.getAllMealsByUser_Id(userId)).thenReturn(List.of(new Meal(), new Meal()));

        List<Meal> result = mealService.getAllMealsByUserId(userId);

        assertEquals(2, result.size());
    }

    @Test
    void getAllPublicMeals_ReturnsList() {
        when(mealRepository.findByIsPublicTrue()).thenReturn(List.of(new Meal()));

        List<Meal> result = mealService.getAllPublicMeals();

        assertEquals(1, result.size());
    }
}